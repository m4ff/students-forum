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
    private final String name;
    private final String mime;
    private final int size;
    
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
    
    public String getSizeString() {
        if(size >= 1024 * 1024) {
            return String.format("%.2f%n", (float) size / 1024 / 1024) + " MB";
        } else if(size >= 1024) {
            return String.format("%.2f%n", (float) size / 1024) + " KB";
        } else {
            return size + " bytes";
        }
    }
    
    public String getMime() {
        return mime;
    }
}
