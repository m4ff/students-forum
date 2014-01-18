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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author paolo
 */
public class LoginFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        DBManager dbmanager = (DBManager) request.getServletContext().getAttribute("dbmanager");
        Cookie[] cookies = request.getCookies();
        User user = null;
        boolean isModerator = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    user = dbmanager.getUser(cookie.getValue());
                    if (user != null) {
                        isModerator = user.getIfModerator();
                        //reset cookie max age
                        cookie.setMaxAge(servlet.Login.MAX_COOKIE_AGE);
                    }
                    break;
                }
            }
        }
        request.setAttribute("isModerator", isModerator);
        request.setAttribute("user", user);
        chain.doFilter(request, response);
    }

}
