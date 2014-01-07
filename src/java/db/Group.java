/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import javax.servlet.ServletContext;

/**
 *
 * @author paolo
 */
public class Group {
    private String name;
    private int id;
    private int creator;

    
    public Group(int id, String name, int creator) {
        this.name = name;
        this.id = id;
        this.creator = creator;
    }
    
    public String getName() {
        return name;
    }
    
    public int getId() {
        return id;
    }
    
    public int getCreator() {
        return creator;
    }
    
    public String getFilesRealPath(HttpServletRequest request) {
        return request.getServletContext().getRealPath("/static/files/" + id);
    }
    
    public boolean hasFileNamed(HttpServletRequest request, String name) {
        String groupFilesPath = getFilesRealPath(request);
        return new File(groupFilesPath + File.separator + name).exists();
    }
    
    //create the directory where to store the uploaded files
    public static boolean createFilesDirecotry(ServletContext context, int groupId) {
        String filesPath = context.getRealPath("/static/files");
        return new File(filesPath + File.separator + groupId).mkdir();
    }
}
