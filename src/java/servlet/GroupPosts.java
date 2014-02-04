/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import db.DBManager;
import db.Group;
import db.Post;
import db.User;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Pier DAgostino
 */
@WebServlet(name = "GroupPosts", urlPatterns = {"/group-posts"})
public class GroupPosts extends HttpServlet {

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
        DBManager dbmanager = (DBManager) request.getServletContext().getAttribute("dbmanager");
        try {
            String groupIdParam = request.getParameter("id");
            int groupId;
            try {
                groupId = Integer.parseInt(groupIdParam);
            } catch(NumberFormatException e) {
                response.sendError(404);
                return;
            }
            Group viewing = dbmanager.getGroup(groupId);
            if(viewing == null) {
                response.sendError(404);
                return;
            }
            LinkedList<Post> groupPosts = dbmanager.getGroupPosts(viewing);
            User user = (User) request.getAttribute("user");
            int userId = user != null ? user.getId() : 0;
            String groupName = viewing.getName();
            request.getAttribute("qrtext");
            request.setAttribute("dbmanager", dbmanager);
            request.setAttribute("posts", groupPosts);
            request.setAttribute("groupId", groupId);
            request.setAttribute("isClosed", viewing.isClosed());
            request.setAttribute("groupName", groupName);
            request.setAttribute("isPublic", !dbmanager.canWrite(userId, groupId));
            request.getRequestDispatcher("groupPosts.jsp").forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(GroupsPage.class.getName()).log(Level.SEVERE, null, ex);
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
        doGet(request, response);
    }

}
