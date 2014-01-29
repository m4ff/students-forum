/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import db.DBManager;
import db.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
public class Account extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/account.jsp").forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DBManager dbmanager = (DBManager) getServletContext().getAttribute("dbmanager");
        String avatarsTmpPath = (String) getServletContext().getAttribute("avatarsTmpDir");
        String avatarsPath = (String) getServletContext().getAttribute("avatarsDir");
        User user = (User) request.getAttribute("user");
        MultipartRequest multipart = new MultipartRequest(request, avatarsTmpPath, 1 * 1024 * 1024, new DefaultFileRenamePolicy());
        File avatar = multipart.getFile("avatar");
        String password = multipart.getParameter("current-password");
        String newPassword = multipart.getParameter("password");
        String error = null;
        if(dbmanager.authenticate(user.getName(), password) == null) {
            error = "The provided password is incorrect";
        } else {
            if(avatar != null) {
                Files.move(avatar.toPath(), new File(avatarsPath + File.separator + user.getId() + ".jpg").toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            if(newPassword != null && !"".equals(newPassword)) {
                if(!dbmanager.updateUserPassword(user, newPassword)) {
                    error = "An error occurred, please try again later";
                }
            }
        }
        request.setAttribute("success", error == null);
        request.setAttribute("error", error);
        doGet(request, response);
    }
}
