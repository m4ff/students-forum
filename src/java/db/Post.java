/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import java.sql.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author paolo
 */
public class Post {
    private final int id;
    private final String text;
    private final Date date;
    private final User creator;
    private final Group group;

   
    
    public Post(int id, String text, Date date, User creator, HashMap<String, GroupFile> groupFiles, Group group) {
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        String linkPattern = "\\$\\$([^\\s]+)\\$\\$";
        String QRPattern = "\\$QR\\$([^\\s]+)\\$\\$";
        //Find links & QRlinks
        Pattern p = Pattern.compile(linkPattern);
        Matcher m = p.matcher(text);
        Pattern pQR = Pattern.compile(QRPattern);
        Matcher mQR = pQR.matcher(text);
        while(m.find()) {
            String g = m.group(1);
            String rep;
            if(groupFiles.get(g) != null) {
                rep = "<a target=\"_blank\" href=\"" + group.getFilePath(g) + "\">" + g + "</a>";
            } else {
                if(g.startsWith("http://") || g.startsWith("https://") || g.startsWith("ftp://") || g.startsWith("ftp://") || g.startsWith("ftps://")) {
                    rep = "<a target=\"_blank\" href=\"" + g + "\">" + g + "</a>";
                } else {
                    rep = "<a target=\"_blank\" href=\"http://" + g + "\">" + g + "</a>";
                }
            }
            text = text.replaceFirst(linkPattern, rep);
        }
        int qrId = 0;
        while(mQR.find()) {
            String g = mQR.group(1);
            String rep;
            if(groupFiles.get(g) != null) {
                rep = "<a target=\"_blank\" href=\"" + group.getFilePath(g) + "\">" + g + "</a>";
            } else {
                if(g.startsWith("http://") || g.startsWith("https://") || g.startsWith("ftp://") || g.startsWith("ftp://") || g.startsWith("ftps://")) {
                    rep = "<a target=\"_blank\" href=\"" + g + "\">" + g + "</a>";
                } else {
                    rep = "<a target=\"_blank\" href=\"http://" + g + "\">" + g + "</a>";
                }
            }
            rep += " (<a href=\"#post-qr-" + qrId + "\" data-rel=\"popup\" data-position-to=\"window\" data-transition=\"fade\">QR</a>)";
            rep += "<div data-role=\"popup\" id=\"post-qr-" + qrId + "\" data-overlay-theme=\"b\" data-theme=\"b\" data-corners=\"false\">\n" +
                    "<a href=\"#\" data-rel=\"back\" class=\"ui-btn ui-corner-all ui-shadow ui-btn-a ui-icon-delete ui-btn-icon-notext ui-btn-right\">Close</a>" +
                    "<img style=\"width: 250px; height: 250px\" src=\"/qr-gen?qrtext=" + g +"\">\n" +
                    "</div>";
            text = text.replaceFirst(QRPattern, rep);
            qrId++;
        }
        //Find emails
        
        this.id = id;
        this.text = text;
        this.date = date;
        this.creator = creator;
        this.group = group;
    }
    
    public Group getGroup() {
        return group;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getText(boolean isPublic) {
        String _text = text;
        if(isPublic) {
            //hide email domain
            String pattern = "[^\\s]+@(([a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9]|[a-zA-Z0-9])(\\.([a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9]|[a-zA-Z0-9]))+)";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(text);
            while (m.find()) {
                String g = m.group(1);
                _text = _text.replaceFirst(g, "xxxxxx");
            }
        }
        return _text;
    }
    
    public String getText() {
        return getText(true);
    }
    
    public Date getDate() {
        return date;
    }
    
    public User getCreator() {
        return creator;
    }
}
