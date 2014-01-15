/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import db.DBManager;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DBManager dbmanager = (DBManager) getServletContext().getAttribute("dbmanager");
        String avatarsPath = getServletContext().getRealPath("/") + ".." + File.separator;
        MultipartRequest multipart = new MultipartRequest(request, avatarsPath, 1 * 1024 * 1024, "UTF-8");
        String email = multipart.getParameter("email");
        String password = multipart.getParameter("password1");
        String username = multipart.getParameter("username");
        System.out.println(email + " " + password + " " + username + " " + avatarsPath);
        String error = null;
        if(password == null || "".equals(password)) {
            error = "Please provide a password";
        } else if(!password.equals(multipart.getParameter("password2"))) {
            error = "Passwords don't match";
        } else if(email == null || "".equals(email)) {
            error = "Please provide an email";
        } else if(username == null || "".equals(username)) {
            error = "Please provide a user name";
        } else {
            try {
                password = new String(MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8")));
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(dbmanager.addUser(email, username, password)) {
                try {
                    response.sendRedirect("/login");
                } catch (IOException ex) {
                    Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                error = "An error occured, please try again later";
            }
        }
        if(error != null) {
            request.setAttribute("error", error);
            doGet(request, response);
        }
    }
}
