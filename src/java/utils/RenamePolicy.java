/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import com.oreilly.servlet.multipart.FileRenamePolicy;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try {
            Path p = Files.move(file.toPath(), new File(path).toPath(), StandardCopyOption.REPLACE_EXISTING);
            return p.toFile();
        } catch (IOException ex) {
            Logger.getLogger(RenamePolicy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
