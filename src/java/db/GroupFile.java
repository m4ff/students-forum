/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

/**
 *
 * @author paolo
 */
public class GroupFile {
    private String name;
    private String mime;
    private int size;
    
    public GroupFile(String name, String mime, int size) {
        this.name = name;
        this.mime = mime;
        this.size = size;
    }
    
    public int getSize() {
        return size;
    }
    
    public String getName() {
        return name;
    }
    
    public String getMime() {
        return mime;
    }
}
