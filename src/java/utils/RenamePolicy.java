/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import com.oreilly.servlet.multipart.FileRenamePolicy;
import java.io.File;

/**
 *
 * @author paolo
 */
public class RenamePolicy implements FileRenamePolicy{
    
    private String path;
    
    public RenamePolicy(String path) {
        this.path = path;
    }

    @Override
    public File rename(File file) {
        file.renameTo(new File(path));
        return file;
    }
    
}
