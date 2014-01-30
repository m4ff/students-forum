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
    private boolean pub = false;
    private int userCount = 0;
    
    public Group(int id, String name, int creator, int count, int userCount, boolean pub) {
        this.name = name;
        this.id = id;
        this.creator = creator;
        this.postsCount = count;
        this.pub = pub;
        this.userCount = userCount;
    }
    
    public Group(int id, String name, int creator, int count) {
        this.name = name;
        this.id = id;
        this.creator = creator;
        this.postsCount = count;
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
    
    public int getId() {
        return id;
    }
    
    public int getCreator() {
        return creator;
    }
    
    public int getPostsCount() {
        return postsCount;
    }
    
    public String getFilesRealPath(HttpServletRequest request) {
        return request.getServletContext().getRealPath("/static/files/" + id);
    }
    
    public boolean hasFileNamed(HttpServletRequest request, String name) {
        String groupFilesPath = getFilesRealPath(request);
        return new File(groupFilesPath + File.separator + name).exists();
    }
    
    //create the directory where to store the uploaded files
    public static boolean createFilesDirectory(ServletContext context, int groupId) {
        String filesPath = context.getRealPath("/static/files");
        return new File(filesPath + File.separator + groupId).mkdir();
    }
}
