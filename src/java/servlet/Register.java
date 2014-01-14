/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import db.DBManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author paolo
 */
public class Register extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        DBManager dbmanager = (DBManager) getServletContext().getAttribute("dbmanager");
        String email = request.getParameter("email");
        String password = request.getParameter("password1");
        String username = request.getParameter("username");
        String error = null;
        if("".equals(password)) {
            error = "Please provide a password";
        } else if(!password.equals(request.getParameter("password2"))) {
            error = "Passwords don't match";
        } else if("".equals(email)) {
            error = "Please provide an email";
        } else if("".equals(username)) {
            error = "Please provide a user name";
        } else {
            dbmanager.addUser(email, username, password);
            try {
                request.getRequestDispatcher("/login").forward(request, response);
            } catch (ServletException ex) {
                Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(error != null) {
            request.setAttribute("error", error);
            doGet(request, response);
        }
    }
}
