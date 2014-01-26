/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
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
@WebServlet(name = "PostPage", urlPatterns = {"/post"})
public class PostPage extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DBManager manager = (DBManager) getServletContext().getAttribute("dbmanager");
        User logged = (User) request.getServletContext().getAttribute("user");
        try {
            String groupIdParam = request.getParameter("id");
            int groupId = groupIdParam != null ? Integer.parseInt(groupIdParam) : 0;
            Group viewing = manager.getGroup(groupId);
            LinkedList<Post> groupPosts = manager.getGroupPosts(viewing);
            request.setAttribute("posts", groupPosts);
            request.getRequestDispatcher("post.jsp").forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(GroupPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DBManager dbmanager = (DBManager) getServletContext().getAttribute("dbmanager");
        User user = (User) request.getSession().getAttribute("user_id");
        String groupIdParam = request.getParameter("id");
        int groupId = groupIdParam != null ? Integer.parseInt(groupIdParam) : 0;
        Group group = dbmanager.getGroup(groupId);
        MultipartRequest multipart = new MultipartRequest(request, group.getFilesRealPath(request), 10 * 1024 * 1024, "UTF-8", new DefaultFileRenamePolicy());
        String text = multipart.getParameter("text");
        dbmanager.addGroupFiles(group, multipart);
        dbmanager.addPost(groupId, user.getId(), text);
        response.sendRedirect("/forum/group?id=" + groupId);
        request.getRequestDispatcher("post.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
