/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 *
 * @author paolo
 */
public class User implements Serializable{
    
    private int id;
    private String name;
    private boolean isModerator;
    
    public User(int id, String name, boolean isModerator) {
        this.id = id;
        this.name = name;
        this.isModerator = isModerator;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getAvatar(HttpServletRequest request) {
        File f = new File(request.getServletContext().getRealPath("/static/avatars") + File.separator + id + ".jpg");
        if(f.exists()) {
            return "/static/avatars/" + id + ".jpg";
        } else {
            return "/static/avatars/0.jpg";
        }
    }
    
    public boolean getIfModerator() {
        return isModerator;
    }
}
