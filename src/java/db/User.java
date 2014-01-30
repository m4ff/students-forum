/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import java.io.Serializable;

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
    
    public String getAvatar() {
        return "/avatar?id=" + this.id;
    }
    
    public boolean getIfModerator() {
        return isModerator;
    }
}
