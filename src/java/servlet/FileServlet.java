/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import com.google.common.io.Files;
import db.DBManager;
import db.GroupFile;
import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author paolo
 */
public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DBManager dbmanager = (DBManager) request.getServletContext().getAttribute("dbmanager");
        String uri = request.getRequestURI();
        int groupId = Integer.parseInt(uri.substring(uri.lastIndexOf('/') + 1, uri.indexOf('-')));
        String fileName = uri.substring(uri.indexOf('-') + 1);
        GroupFile f = dbmanager.getFile(groupId, fileName);
        
        response.setContentType(f.getMime());
        response.setContentLength(f.getSize());
        
        String path = (String) getServletContext().getAttribute("filesDir") + File.separator + groupId + File.separator + fileName;
        response.getOutputStream().write(Files.toByteArray(new File(path)));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
