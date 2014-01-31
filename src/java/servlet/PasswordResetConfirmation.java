/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import db.DBManager;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *
 * @author halfblood
 */
public class PasswordResetConfirmation extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager manager = (DBManager) getServletContext().getAttribute("dbmanager");

        String email = (String) request.getParameter("email");
        String hex = (String) request.getParameter("x");
        boolean correct = false;
        if (manager.emailAndCodeInDatabase(email, hex)) {
            Date lastTime = manager.getLastVerificationCodeTime(email);
            Date now = new Date();
            long timeDifference = now.getTime() - lastTime.getTime();
            if (timeDifference <= 90000) {
                correct = true;
            }
        }
        request.setAttribute("isCorrect", correct);
        request.getRequestDispatcher("/passwordResetConfirmation.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager manager = (DBManager) getServletContext().getAttribute("dbmanager");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        manager.updateUserPassword(email, password);
        response.sendRedirect("/login?changed=yes");
    }

}
