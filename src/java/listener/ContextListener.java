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
        
        String projectDir = sce.getServletContext().getRealPath("/") + ".." + File.separator + ".." + File.separator;
        
        String avatarsDir = projectDir + "avatars";
        String avatarsTmpDir = projectDir + "avatarsTmp";
        String filesDir = projectDir + "files";
        
        sce.getServletContext().setAttribute("avatarsDir", avatarsDir);
        sce.getServletContext().setAttribute("avatarsTmpDir", avatarsTmpDir);
        sce.getServletContext().setAttribute("filesDir", filesDir);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBManager.shutdown();
    }
    
}
