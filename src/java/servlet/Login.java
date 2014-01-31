/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import db.DBManager;
import db.User;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author paolo
 */
public class Login extends HttpServlet{
    public final static int MAX_COOKIE_AGE = 60 * 60 * 24 * 7; //a week in secondes
    public final static int MAX_LOGIN_TIME_COOKIE_AGE = 60 * 60 * 24 * 365; //a year in secondes
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            boolean confirmation = false;
            String changed = (String) request.getParameter("changed");
            if(changed != null){
                confirmation = true;
            }
            request.setAttribute("confirmation", confirmation);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        DBManager dbmanager = (DBManager) getServletContext().getAttribute("dbmanager");
        User user = dbmanager.authenticate(request.getParameter("username"), request.getParameter("password"));
        boolean rememberMe = request.getParameter("remember") != null;
        String redirect = (String) request.getAttribute("redirect");
        if(user != null) {
            Cookie loginTime = new Cookie("loginTime", new Date().getTime() + "");
            loginTime.setMaxAge(MAX_LOGIN_TIME_COOKIE_AGE);
            loginTime.setPath("/");
            loginTime.setHttpOnly(true);
            
            Cookie userCookie = new Cookie("userId", user.getId() + "");
            userCookie.setMaxAge(rememberMe ? MAX_COOKIE_AGE : -1);
            userCookie.setPath("/");
            userCookie.setHttpOnly(true);
            
            response.addCookie(userCookie);
            response.addCookie(loginTime);
            try {
                if(redirect != null) {
                    response.sendRedirect(redirect);
                } else {
                    response.sendRedirect("/");
                }
            } catch (IOException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            request.setAttribute("error", "Login incorrect");
            doGet(request, response);
        }
    }
}
