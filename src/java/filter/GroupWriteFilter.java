/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filter;

import db.DBManager;
import db.Group;
import db.User;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author paolo
 */
public class GroupWriteFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        DBManager dbmanager = (DBManager) request.getServletContext().getAttribute("dbmanager");
        User user = (User) request.getAttribute("user");
        int userId = user != null ? user.getId() : 0;
        int groupId;
        try {
            groupId = Integer.parseInt(request.getParameter("id"));
        } catch(NumberFormatException e) {
            response.sendError(404);
            return;
        }
        
        Group group = dbmanager.getGroup(groupId);
        if(group == null) {
            response.sendError(404);
            return;
        }
        
        if(!dbmanager.canWrite(userId, groupId) || group.isClosed()) {
            response.sendError(403);
        } else {
            chain.doFilter(request, response);
        }
    }
    
}
