/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filter;

import db.DBManager;
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
public class GroupFileFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        DBManager dbmanager = (DBManager) request.getServletContext().getAttribute("dbmanager");
        User user = (User) request.getAttribute("user");
        int userId = user != null ? user.getId() : 0;
        String uri = request.getRequestURI();
        int groupId;
        try {
            groupId = Integer.parseInt(uri.substring(uri.lastIndexOf('/') + 1, uri.indexOf('-')));
        } catch(NumberFormatException e) {
            response.sendError(404);
            return;
        }
        
        if(!dbmanager.canRead(userId, groupId)) {
            response.sendError(403);
        } else {
            chain.doFilter(request, response);
        }
    }
    
}
