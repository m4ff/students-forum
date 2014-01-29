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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import utils.RenamePolicy;

/**
 *
 * @author paolo
 */
public class Register extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        DBManager dbmanager = (DBManager) getServletContext().getAttribute("dbmanager");
        String avatarsTmpPath = (String) getServletContext().getAttribute("avatarsTmpDir");
        String avatarsPath = (String) getServletContext().getAttribute("avatarsDir");
        MultipartRequest multipart = new MultipartRequest(request, avatarsTmpPath, 1 * 1024 * 1024, new DefaultFileRenamePolicy());
        String email = multipart.getParameter("email");
        String password = multipart.getParameter("password1");
        String username = multipart.getParameter("username");
        System.out.println(email + " " + password + " " + username + " " + avatarsPath);
        String error = null;
        if (password == null || "".equals(password)) {
            error = "Please provide a password";
        } else if (!password.equals(multipart.getParameter("password2"))) {
            error = "Passwords don't match";
        } else if (email == null || "".equals(email)) {
            error = "Please provide an email";
        } else if (username == null || "".equals(username)) {
            error = "Please provide a user name";
        } else {
            try {
                password = new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8"))).toString(16);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
            }
            int userId = dbmanager.addUser(email, username, password);
            if (userId != 0) {
                Files.move(multipart.getFile("avatar").toPath(), new File(avatarsPath + File.separator + userId + ".jpg").toPath(), StandardCopyOption.REPLACE_EXISTING);
                response.sendRedirect("/login");
            } else {
                error = "An error occured, please try again later";
            }
        }
        if (error != null) {
            request.setAttribute("error", error);
            doGet(request, response);
        }
    }
    
    
    
    public static void handleUpload(HttpServletRequest request, String paramName, String newFilePathWithName) throws IOException, ServletException {
        final Part filePart = request.getPart(paramName);

        OutputStream out = null;
        InputStream filecontent = null;

        try {
            out = new FileOutputStream(new File(newFilePathWithName));
            filecontent = filePart.getInputStream();
            int read;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } catch (FileNotFoundException fne) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, fne);
        } finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
        }
    }
}
