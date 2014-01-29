/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package listener;

import db.DBManager;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author paolo
 */
public class ContextListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DBManager manager = new DBManager();
            sce.getServletContext().setAttribute("dbmanager", manager);
        } catch (SQLException ex) {
            Logger.getLogger(ContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Creates the directory where to store the uploaded files
        String contextPath = sce.getServletContext().getRealPath("/static");
        File groupFilesDirectory = new File(contextPath + File.separator + "files");
        groupFilesDirectory.mkdir();
        
        String avatarsDir = sce.getServletContext().getRealPath("/") + ".." + File.separator + "avatars";
        String avatarsTmpDir = sce.getServletContext().getRealPath("/") + ".." + File.separator + "avatarsTmp";
        new File(avatarsDir).mkdir();
        new File(avatarsTmpDir).mkdir();
        sce.getServletContext().setAttribute("avatarsDir", avatarsDir);
        sce.getServletContext().setAttribute("avatarsTmpDir", avatarsTmpDir);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBManager.shutdown();
    }
    
}
