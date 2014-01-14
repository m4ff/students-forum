/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

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
        String email = request.getParameter("email");
        String password = request.getParameter("password1");
        String username = request.getParameter("username");
        if("".equals(password)) {
            request.setAttribute("error", "1");
            request.setAttribute("error_msq", "Please provide a password");
        } else if(!password.equals(request.getParameter("password2"))) {
            request.setAttribute("error", "1");
            request.setAttribute("error_msq", "Passwords don't match");
        } else if("".equals(email)) {
            request.setAttribute("error", "1");
            request.setAttribute("error_msq", "Please provide an email");
        } else if("".equals(username)) {
            request.setAttribute("error", "1");
            request.setAttribute("error_msq", "Please provide a user name");
        }
        doGet(request, response);
    }
}
