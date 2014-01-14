/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import com.oreilly.servlet.MultipartRequest;
import java.io.File;
import java.io.Serializable;
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

/**
 *
 * @author paolo
 */
public class DBManager implements Serializable {

    private Connection connection;

    public DBManager() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver", true, getClass().getClassLoader());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.connection = DriverManager.getConnection("jdbc:derby://localhost:1527/SimpleForum", "nan", "nan");
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
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setString(1, userName);
                stm.setString(2, password);
                ResultSet res = stm.executeQuery();
                try {
                    if (res.next()) {
                        u = new User(res.getInt("user_id"), res.getString("user_name"), res.getBoolean("user_moderator"));
                    }
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }
    
    public void addUser(String email, String username, String password) {
        try {
            String query = "INSERT INTO \"user\"(user_email, user_name, user_password) VALUES(?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setString(1, email);
                stm.setString(2, username);
                stm.setString(3, password);
                stm.executeUpdate();
            } finally {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public LinkedList<Group> getUserGroups(User u) {
        LinkedList<Group> g = new LinkedList<>();
        try {
            String query = "SELECT COUNT(group_id) AS group_post_count, * FROM \"user_group\" NATURAL JOIN \"group\" WHERE user_id = ?";
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, u.getId());
                ResultSet res = stm.executeQuery();
                try {
                    while (res.next()) {
                        g.add(
                                new Group(
                                        res.getInt("group_id"),
                                        res.getString("group_name"),
                                        res.getInt("creator_id"),
                                        res.getInt("group_post_count")
                                )
                        );
                    }
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
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
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, g.getId());
                ResultSet res = stm.executeQuery();
                try {
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
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
    
    public int getPostsNumber(Group g) {
        int count = 0;
        try {
            String query = "SELECT COUNT('post_id') FROM \"group\" NATURAL JOIN \"post\" WHERE group_id = ?";
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, g.getId());
                ResultSet res = stm.executeQuery();
                try {
                    while (res.next()) {
                        count = Integer.parseInt(res.getString(query));
                    }
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
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
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, p.getId());
                ResultSet res = stm.executeQuery();
                try {
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
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
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
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, g.getId());
                ResultSet res = stm.executeQuery();
                try {
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
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
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
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, group.getId());
                ResultSet res = stm.executeQuery();

                try {
                    res.next();
                    date = res.getTimestamp("max_date");
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public Group getGroup(int groupId) {
        Group target = null;
        try {
            String query = "SELECT COUNT(*) AS group_post_count, * FROM \"group\" WHERE group_id = ?";
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, groupId);
                ResultSet res = stm.executeQuery();

                try {
                    if (res.next()) {
                        target = new Group(
                                res.getInt("group_id"),
                                res.getString("group_name"),
                                res.getInt("creator_id"),
                                res.getInt("group_post_count")
                        );
                    }
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
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
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, group);
                ResultSet res = stm.executeQuery();
                try {
                    while (res.next()) {
                        u.add(
                                new User(
                                        res.getInt("user_id"),
                                        res.getString("user_name"),
                                        res.getBoolean("user_moderator")
                                )
                        );
                    }
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
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
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, group);
                ResultSet res = stm.executeQuery();
                try {
                    while (res.next()) {
                        u.add(
                                new User(
                                        res.getInt("user_id"),
                                        res.getString("user_name"),
                                        res.getBoolean("user_moderator")
                                )
                        );
                    }
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
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
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, group);
                ResultSet res = stm.executeQuery();
                try {
                    while (res.next()) {
                        u.add(
                                new User(
                                        res.getInt("user_id"),
                                        res.getString("user_name"),
                                        res.getBoolean("user_moderator")
                                )
                        );
                    }
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
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
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, user);
                ResultSet res = stm.executeQuery();
                try {
                    res.next();
                    u = new Group(
                            res.getInt("group_id"),
                            res.getString("group_name"),
                            res.getInt("creator_id"),
                            0
                    );
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public void changeGroupName(int group, String name) {
        try {
            String query = "UPDATE \"group\" SET group_name = ? WHERE group_id = ?";
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setString(1, name);
                stm.setInt(2, group);
                stm.executeUpdate();
            } finally {
                stm.close();
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
                    } catch (Exception e) {
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
            PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            try {
                stm.setInt(1, creator);
                stm.setString(2, name);
                stm.executeUpdate();
                ResultSet rs = stm.getGeneratedKeys();
                if (rs != null) {
                    if (rs.next()) {
                        groupId = rs.getInt(1);
                    }
                }
            } finally {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (groupId > 0) {
            try {
                String query = "INSERT INTO \"user_group\"(group_accepted, group_id, visible, user_id) VALUES(?, ?, ?, ?)";
                PreparedStatement stm = connection.prepareStatement(query);
                try {
                    stm.setBoolean(1, true);
                    stm.setInt(2, groupId);
                    stm.setBoolean(3, true);
                    stm.setInt(4, creator);
                    stm.executeUpdate();
                } finally {
                    stm.close();
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
            PreparedStatement stm = connection.prepareStatement(query);
            try {
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
            } finally {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addPost(int groupId, int userId, String text) {
        try {
            String query = "INSERT INTO \"post\"(user_id, group_id, post_text) VALUES(?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, userId);
                stm.setInt(2, groupId);
                stm.setString(3, text);
                stm.executeUpdate();
            } finally {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void acceptInvitesFromGroups(Map<String, String[]> m, int user) {
        try {
            String query = "UPDATE \"user_group\" SET group_accepted = TRUE WHERE group_id = ? AND user_id = ?";
            PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            try {
                for (Map.Entry<String, String[]> entry : m.entrySet()) {
                    String key = entry.getKey();
                    try {
                        int groupId = Integer.parseInt(key);
                        String[] value = entry.getValue();
                        switch (value[0]) {
                            case "accepted":
                                stm.setInt(1, groupId);
                                stm.setInt(2, user);
                                stm.executeUpdate();
                                break;
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

    public User getUser(int userId) {
        User user = null;
        try {
            String query = "SELECT * FROM \"user\" WHERE user_id = ?";
            PreparedStatement stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, userId);
                ResultSet res = stm.executeQuery();
                try {
                    if (res.next()) {
                        user = new User(
                                res.getInt("user_id"),
                                res.getString("user_name"),
                                res.getBoolean("user_moderator")
                        );
                    }
                } finally {
                    res.close();
                }
            } finally {
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return x;
    }
}
