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
    private final String name;
    private final int id;
    private final int creator;
    private final int postsCount;
    private final boolean pub;
    private final int userCount;
    private final boolean closed;
    
    public Group(int id, String name, int creator, int postsCount, int userCount, boolean pub, boolean closed) {
        this.name = name;
        this.id = id;
        this.creator = creator;
        this.postsCount = postsCount;
        this.pub = pub;
        this.userCount = userCount;
        this.closed = closed;
    }
    
    public Group(int id, String name, int creator) {
        this.name = name;
        this.id = id;
        this.creator = creator;
        this.postsCount = 0;
        this.pub = false;
        this.userCount = 0;
        this.closed = false;
    }
    
    public String getName() {
        return name;
    }
    
    public int getUserCount() {
        return userCount;
    }
    
    public boolean isPublic() {
        return pub;
    }
    
    public boolean isClosed() {
        return closed;
    }
    
    public int getId() {
        return id;
    }
    
    public int getCreator() {
        return creator;
    }
    
    public int getPostsCount() {
        return postsCount;
    }
    
    public String getFilesRealPath(ServletContext context) {
        return context.getAttribute("filesDir") + File.separator + id;
    }
    
    public boolean hasFileNamed(ServletContext context, String name) {
        String groupFilesPath = getFilesRealPath(context);
        return new File(groupFilesPath + File.separator + name).exists();
    }
    
    //create the directory where to store the uploaded files
    public static boolean createFilesDirectory(ServletContext context, int groupId) {
        String filesPath = (String) context.getAttribute("filesDir");
        return new File(filesPath + File.separator + groupId).mkdir();
    }
}
