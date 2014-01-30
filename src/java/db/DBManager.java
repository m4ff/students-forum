/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import com.oreilly.servlet.MultipartRequest;
import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

/**
 *
 * @author paolo
 */
public class DBManager implements Serializable {

    private final Connection connection;

    public DBManager() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver", true, getClass().getClassLoader());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.connection = DriverManager.getConnection("jdbc:derby://localhost:1527/students_forum", "students_forum", "password");
    }
    
    public static String hashPassword(String password) {
        try {
            return new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8"))).toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User authenticate(String userName, String password) {
        User u = null;
        try {
            String query = "SELECT * FROM \"user\" WHERE user_name = ? AND user_password = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                password = hashPassword(password);
                stm.setString(1, userName);
                stm.setString(2, password);
                try (ResultSet res = stm.executeQuery()) {
                    if (res.next()) {
                        u = new User(res.getInt("user_id"), res.getString("user_name"), res.getBoolean("user_moderator"));
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public int addUser(String email, String username, String password) {
        int userId = 0;
        try {
            String query = "INSERT INTO \"user\"(user_email, user_name, user_password) VALUES(?, ?, ?)";
            try (PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                password = hashPassword(password);
                stm.setString(1, email);
                stm.setString(2, username);
                stm.setString(3, password);
                stm.executeUpdate();
                ResultSet rs = stm.getGeneratedKeys();
                if (rs != null) {
                    if (rs.next()) {
                        userId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userId;
    }
    
    public boolean updateUserPassword(User user, String password) {
        int userId = 0;
        password = hashPassword(password);
        try {
            String query = "UPDATE \"user\" SET user_password = ? WHERE user_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stm.setString(1, password);
                stm.setInt(2, user.getId());
                return stm.executeUpdate() == 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public LinkedList<Group> getGroups() {
        LinkedList<Group> g = new LinkedList<>();
        try {
            String query = "SELECT * FROM (SELECT group_id, COUNT(post_id) AS post_count, COUNT(DISTINCT user_id) AS user_count  FROM \"group\" NATURAL JOIN \"post\" GROUP BY group_id) t NATURAL JOIN \"group\"";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                try (ResultSet res = stm.executeQuery()) {
                    while (res.next()) {
                        g.add(
                                new Group(
                                        res.getInt("group_id"),
                                        res.getString("group_name"),
                                        res.getInt("creator_id"),
                                        res.getInt("post_count"),
                                        res.getInt("user_count"),
                                        res.getBoolean("group_public"),
                                        res.getBoolean("group_closed")
                                )
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return g;
    }

    public LinkedList<Group> getUserGroups(User u) {
        LinkedList<Group> g = new LinkedList<>();
        try {
            String query = "SELECT * FROM (SELECT group_id, COUNT(post_id) AS post_count, COUNT(DISTINCT user_id) AS user_count  FROM \"group\" NATURAL JOIN \"post\" GROUP BY group_id) t NATURAL JOIN \"group\" NATURAL JOIN \"user_group\" WHERE user_id = ? AND group_accepted = TRUE";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, u.getId());
                try (ResultSet res = stm.executeQuery()) {
                    while (res.next()) {
                        g.add(
                                new Group(
                                        res.getInt("group_id"),
                                        res.getString("group_name"),
                                        res.getInt("creator_id"),
                                        res.getInt("post_count"),
                                        res.getInt("user_count"),
                                        res.getBoolean("group_public"),
                                        res.getBoolean("group_closed")
                                )
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return g;
    }

    public LinkedList<Post> getGroupPosts(Group g) {
        LinkedList<Post> p = new LinkedList<>();
        try {
            String query = "SELECT * FROM \"post\" NATURAL JOIN \"user\" WHERE group_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, g.getId());
                try (ResultSet res = stm.executeQuery()) {
                    HashMap<String, GroupFile> files = getGroupFiles(g);
                    while (res.next()) {
                        p.add(
                                new Post(
                                        res.getInt("post_id"),
                                        res.getString("post_text"),
                                        res.getDate("post_date"),
                                        new User(res.getInt("user_id"), res.getString("user_name"), res.getBoolean("user_moderator")),
                                        files,
                                        g
                                )
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
    
    
    public int getPostsNumber(Group g, User logged) {
        int count = 0;
        try {
            String query = "SELECT COUNT(post_id) AS count FROM \"post\" NATURAL JOIN \"group\" WHERE group_id = ? AND user_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, g.getId());
                stm.setInt(2, logged.getId());
                try (ResultSet res = stm.executeQuery()) {
                    while (res.next()) {
                        count = res.getInt("count");
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public HashMap<String, GroupFile> getPostFiles(Post p) {
        HashMap<String, GroupFile> f = new HashMap<>();
        try {
            String query = "SELECT * FROM \"file\" NATURAL JOIN \"post\" WHERE post_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, p.getId());
                try (ResultSet res = stm.executeQuery()) {
                    while (res.next()) {
                        f.put(
                                res.getString("file_name"),
                                new GroupFile(
                                        res.getString("file_name"),
                                        res.getString("file_mime"),
                                        res.getInt("file_size")
                                )
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return f;
    }

    public HashMap<String, GroupFile> getGroupFiles(Group g) {
        HashMap<String, GroupFile> f = new HashMap<>();
        try {
            String query = "SELECT * FROM \"file\" NATURAL JOIN \"post\" NATURAL JOIN \"group\" WHERE group_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, g.getId());
                try (ResultSet res = stm.executeQuery()) {
                    while (res.next()) {
                        f.put(
                                res.getString("file_name"),
                                new GroupFile(
                                        res.getString("file_name"),
                                        res.getString("file_mime"),
                                        res.getInt("file_size")
                                )
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return f;
    }

    public Timestamp getLatestPost(Group group) {
        Timestamp date = null;
        try {
            String query = "SELECT MAX(post_date) AS max_date FROM \"post\" WHERE group_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, group.getId());
                try (ResultSet res = stm.executeQuery()) {
                    res.next();
                    date = res.getTimestamp("max_date");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public Group getGroup(int groupId) {
        Group target = null;
        try {
            String query = "SELECT group_id, group_name, creator_id FROM \"group\" WHERE group_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, groupId);

                try (ResultSet res = stm.executeQuery()) {
                    if (res.next()) {
                        target = new Group(
                                res.getInt("group_id"),
                                res.getString("group_name"),
                                res.getInt("creator_id"),
                                0
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return target;
    }

    public LinkedList<Group> getInvites(User user) {
        LinkedList<Group> u = new LinkedList<>();
        try {
            String query = "SELECT * FROM \"user_group\" NATURAL JOIN \"group\" WHERE user_id = ? AND group_accepted = FALSE";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, user.getId());
                try (ResultSet res = stm.executeQuery()) {
                    while (res.next()) {
                        u.add(
                                new Group(
                                        res.getInt("group_id"),
                                        res.getString("group_name"),
                                        res.getInt("creator_id"),
                                        0
                                )
                        );
                    }
                    res.close();
                }
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public LinkedList<User> getUsersForGroupAndVisible(int group) {
        LinkedList<User> u = new LinkedList<>();
        try {
            //togliere il creatore del gruppo dalla richiesta
            String query = "SELECT * FROM (SELECT * FROM \"group\" NATURAL JOIN \"user_group\" WHERE group_id = ? AND visible = TRUE) t natural join \"user\"";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, group);
                try (ResultSet res = stm.executeQuery()) {
                    while (res.next()) {
                        u.add(
                                new User(
                                        res.getInt("user_id"),
                                        res.getString("user_name"),
                                        res.getBoolean("user_moderator")
                                )
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public LinkedList<User> getUsersForGroupAndNotVisible(int group) {
        LinkedList<User> u = new LinkedList<>();
        try {
            String query = "SELECT * FROM (SELECT * FROM \"group\" NATURAL JOIN \"user_group\" WHERE group_id = ? AND visible = FALSE) t natural join \"user\"";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, group);
                try (ResultSet res = stm.executeQuery()) {
                    while (res.next()) {
                        u.add(
                                new User(
                                        res.getInt("user_id"),
                                        res.getString("user_name"),
                                        res.getBoolean("user_moderator")
                                )
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public LinkedList<User> getUsersNotInGroup(int group) {
        LinkedList<User> u = new LinkedList<>();
        try {
            String query = "select * from (select * from \"user_group\" natural join \"group\" where group_id = ?) t natural right outer join \"user\" where t.user_id IS NULL";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, group);
                try (ResultSet res = stm.executeQuery()) {
                    while (res.next()) {
                        u.add(
                                new User(
                                        res.getInt("user_id"),
                                        res.getString("user_name"),
                                        res.getBoolean("user_moderator")
                                )
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public Group getGroupMadeByUser(int user) {
        Group u = null;
        try {
            String query = "SELECT * FROM \"group\" WHERE creator_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, user);
                try (ResultSet res = stm.executeQuery()) {
                    res.next();
                    u = new Group(
                            res.getInt("group_id"),
                            res.getString("group_name"),
                            res.getInt("creator_id"),
                            0
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public void changeGroupName(int group, String name) {
        try {
            String query = "UPDATE \"group\" SET group_name = ? WHERE group_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setString(1, name);
                stm.setInt(2, group);
                stm.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateMyGroupValues(int group, Map<String, String[]> m) {
        try {
            String query = "UPDATE \"user_group\" SET visible = ? WHERE group_id = ? AND user_id = ?";
            PreparedStatement stm = connection.prepareStatement(query);
            query = "INSERT INTO \"user_group\"(group_accepted, group_id, visible, user_id) VALUES(?, ?, ?, ?)";
            PreparedStatement stm2 = connection.prepareStatement(query);
            try {
                for (Map.Entry<String, String[]> entry : m.entrySet()) {
                    String key = entry.getKey();
                    try {
                        int userId = Integer.parseInt(key);
                        String[] value = entry.getValue();

                        switch (value[0]) {
                            case "member":
                                stm2.setInt(2, group);
                                stm2.setBoolean(1, false);
                                stm2.setBoolean(3, true);
                                stm2.setInt(4, userId);
                                stm2.executeUpdate();
                                break;
                            case "invisible":
                                stm.setInt(2, group);
                                stm.setBoolean(1, false);
                                stm.setInt(3, userId);
                                stm.executeUpdate();
                                break;
                            case "visible":
                                stm.setInt(2, group);
                                stm.setBoolean(1, true);
                                stm.setInt(3, userId);
                                stm.executeUpdate();

                        }
                    } catch (NumberFormatException | SQLException e) {
                        Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
                    }

                }
            } finally {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int createGroup(int creator, String name) {
        int groupId = 0;
        try {
            String query = "INSERT INTO \"group\"(creator_id, group_name) VALUES(?, ?)";
            try (PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stm.setInt(1, creator);
                stm.setString(2, name);
                stm.executeUpdate();
                ResultSet rs = stm.getGeneratedKeys();
                if (rs != null) {
                    if (rs.next()) {
                        groupId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (groupId > 0) {
            try {
                String query = "INSERT INTO \"user_group\"(group_accepted, group_id, visible, user_id) VALUES(?, ?, ?, ?)";
                try (PreparedStatement stm = connection.prepareStatement(query)) {
                    stm.setBoolean(1, true);
                    stm.setInt(2, groupId);
                    stm.setBoolean(3, true);
                    stm.setInt(4, creator);
                    stm.executeUpdate();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return groupId;
    }

    public void addGroupFiles(Group group, MultipartRequest multipart) {
        Enumeration<String> files = multipart.getFileNames();
        try {
            String query = "INSERT INTO \"file\"(post_id, file_name, file_mime, file_size) VALUES(?, ?, ?, ?)";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                while (files.hasMoreElements()) {
                    String name = files.nextElement();
                    System.out.println(name);
                    String filename = multipart.getFilesystemName(name);
                    System.out.println(filename);
                    String type = multipart.getContentType(name);
                    System.out.println(type);
                    File f = multipart.getFile(name);
                    if (f != null) {
                        stm.setInt(1, group.getId());
                        stm.setString(2, filename);
                        stm.setString(3, type);
                        stm.setLong(4, f.length());
                        stm.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addPost(int groupId, int userId, String text) {
        try {
            String query = "INSERT INTO \"post\"(user_id, group_id, post_text) VALUES(?, ?, ?)";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, userId);
                stm.setInt(2, groupId);
                stm.setString(3, text);
                stm.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void acceptInvitesFromGroups(int group, int user) {
        try {
            String query = "UPDATE \"user_group\" SET group_accepted = TRUE WHERE group_id = ? AND user_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, group);
                stm.setInt(2, user);
                stm.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void declineInvitesFromGroups(int group, int user) {
        try {
            String query = "DELETE FROM \"user_group\" WHERE group_id = ? AND user_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, group);
                stm.setInt(2, user);
                stm.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Date getTime(int user) {
        Date time = null;
        String query = "SELECT user_last_time FROM \"user\" WHERE user_id = ?";
        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, user);
                try(ResultSet res = stm.executeQuery()) {
                    res.next();
                    time = res.getDate("user_last_time");
                }
            } finally {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;
    }
    
    
    public void updateTime(int user) {
        Date time = new Date();
        String query = "UPDATE \"user\" SET user_last_time = ? WHERE user_id = ?";
        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(query);
            try {
                stm.setTimestamp(1, new Timestamp(time.getTime()));
                stm.setInt(2, user);
                stm.executeUpdate();
            } finally {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User getUser(int userId) {
        User user = null;
        try {
            String query = "SELECT * FROM \"user\" WHERE user_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, userId);
                try (ResultSet res = stm.executeQuery()) {
                    if (res.next()) {
                        user = new User(
                                res.getInt("user_id"),
                                res.getString("user_name"),
                                res.getBoolean("user_moderator")
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public User getUser(String userId) {
        return getUser(Integer.parseInt(userId));
    }

    public boolean checkIfUserCanAccessGroup(int userId, int groupId) {
        boolean x = false;
        try {
            String query = "SELECT * FROM \"user_group\" WHERE user_id = ? AND group_id = ? AND visible = FALSE";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, userId);
                stm.setInt(2, groupId);
                ResultSet res = stm.executeQuery();
                x = res.next();

            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return x;
    }

public LinkedList<Post> getPostsFromDate(Date d, User user) {
        LinkedList<Post> p = new LinkedList<>();
        try {
            String query = "SELECT * FROM (SELECT * FROM (SELECT post_id, group_id, post_text, post_date, user_id AS poster_id FROM \"post\") t NATURAL JOIN \"user_group\" WHERE user_id = ? AND group_accepted = TRUE AND post_date >= ? ORDER BY group_id ASC, post_date DESC) t1 JOIN \"user\" ON \"user\".user_id = poster_id";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, user.getId());
                stm.setTimestamp(2, new Timestamp(d.getTime()));
                try (ResultSet res = stm.executeQuery()) {
                    while (res.next()) {
                        Group g = getGroup(res.getInt("group_id"));
                        p.add(
                                new Post(
                                        res.getInt("post_id"),
                                        res.getString("post_text"),
                                        res.getDate("post_date"),
                                        new User(res.getInt("poster_id"), res.getString("user_name"), res.getBoolean("user_moderator")),
                                        null,
                                        g
                                )
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
}
