/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import db.DBManager;
import db.Group;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Pier DAgostino
 */
public class Moderation extends HttpServlet {

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
        DBManager dbmanager = (DBManager) getServletContext().getAttribute("dbmanager");
        LinkedList<Group> groups = dbmanager.getGroups();
        request.setAttribute("dbmanager", dbmanager);
        request.setAttribute("groups", groups);
        try{
            request.getRequestDispatcher("/moderation.jsp").forward(request, response);
        } catch(ServletException | IOException ex) {
            Logger.getLogger(Moderation.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        
    }

}
