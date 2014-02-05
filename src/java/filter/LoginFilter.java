/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import db.DBManager;
import db.User;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        int userId = 0;
        String loginCode = null;
        boolean isModerator = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "userCode":
                        loginCode = cookie.getValue();
                        break;
                    case "userId":
                        userId = Integer.parseInt(cookie.getValue());
                        break;
                    case "loginTime":
                        long time = Long.parseLong(cookie.getValue());
                        
                        request.setAttribute("loginTime", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time)));
                        break;
                }
            }
            if(userId != 0 && loginCode != null) {
                user = dbmanager.authenticate(userId, loginCode);
            }
            if(user != null) {
                isModerator = user.isModerator();
            }
        }
        request.setAttribute("isModerator", isModerator);
        request.setAttribute("user", user);
        chain.doFilter(request, response);
    }

}
