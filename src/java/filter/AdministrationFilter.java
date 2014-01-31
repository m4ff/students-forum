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
public class AdministrationFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        DBManager dbmanager = (DBManager) request.getServletContext().getAttribute("dbmanager");
        User user = (User) request.getAttribute("user");
        String param = request.getParameter("id");
        
        if (param == null || "0".equals(param)) {
            chain.doFilter(request, response);
            return;
        }
        
        int groupId = Integer.parseInt(param);
        if (user != null) {
            Group g = dbmanager.getGroup(groupId);
            if (g != null) {
                if (g.getCreator() == user.getId()) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }
        response.sendError(403);
    }

}
