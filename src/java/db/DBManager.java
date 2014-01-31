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

    /**
     * Apre una connessione al database
     *
     * @throws SQLException
     */
    public DBManager() throws SQLException {
        try {
            //Class.forName("org.apache.derby.jdbc.EmbeddedDriver", true, getClass().getClassLoader());
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.connection = DriverManager.getConnection("jdbc:derby://localhost:1527/students_forum", "students_forum", "password");
    }

    /**
     * Applica la funzione hash SHA256 e ritorna una rappresentazione
     * esadecimale dell'hash
     *
     * @param password
     * @return Hash
     */
    public static String hashPassword(String password) {
        try {
            return new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8"))).toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Chiude la connessione al database
     */
    public static void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
        }
    }

    /**
     * Verifica se username e password esistono nel database e corrispondono
     *
     * @param userName
     * @param password
     * @return Istanza di {@link User} se l'utente viene trovato, null
     * altrimenti
     */
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

    /**
     * Aggiunge un utente al database
     *
     * @param email
     * @param username
     * @param password
     * @return Id dell'utente appena inserito, 0 se l'inserimento fallisce
     */
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

    /**
     * Aggiorna la password dell'utente
     *
     * @param user
     * @param password
     * @return
     */
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

    /**
     * Ritorna una LinkedList di {@link Group} che contiene tutti i gruppi del
     * forum
     *
     * @return
     */
    public LinkedList<Group> getGroups() {
        LinkedList<Group> g = new LinkedList<>();
        try {
            String query = "SELECT DISTINCT * FROM (SELECT DISTINCT group_id, COUNT(DISTINCT post_id) AS post_count, COUNT(DISTINCT user_id) AS user_count  FROM \"group\" NATURAL JOIN \"user_group\" NATURAL LEFT OUTER JOIN \"post\" GROUP BY group_id) t NATURAL JOIN \"group\"";
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

    /**
     * Ritorna un LinkedList di {@link Group} di cui user è membro
     *
     * @param user
     * @return
     */
    public LinkedList<Group> getUserGroups(User user) {
        LinkedList<Group> g = new LinkedList<>();
        try {
            String query = "SELECT DISTINCT * FROM (SELECT DISTINCT group_id, COUNT(post_id) AS post_count, COUNT(DISTINCT user_id) AS user_count  FROM \"group\" NATURAL JOIN \"user_group\" NATURAL LEFT OUTER JOIN \"post\" GROUP BY group_id) t NATURAL JOIN \"group\" NATURAL JOIN \"user_group\" WHERE user_id = ? AND group_accepted = TRUE";
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, user.getId());
            ResultSet res = stm.executeQuery();
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
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return g;
    }

    /**
     * Ritorna una LinkedList di {@link Post} del gruppo passato come parametro
     *
     * @param g
     * @return
     */
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

    /**
     * Ritorna una HashMap con i nomi dei file come chiavi e {@link GroupFile}
     * come valore, i file sono relativi al {@link Post} passato come paramentro
     *
     * @param p
     * @return
     */
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

    /**
     * Ritorna una HashMap con i nomi dei file come chiavi e {@link GroupFile}
     * come valore, i file sono relativi al gruppo passato come paramentro
     *
     * @param g
     * @return
     */
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

    /**
     * Ritorna la data dell'ultimo post del gruppo passato come parametro
     *
     * @param group
     * @return
     */
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

    /**
     * Ritorna un'istanza di {@link Group} con ID groupId, null se groupId non è
     * l'ID di alcun gruppo
     *
     * @param groupId
     * @return
     */
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
                                res.getInt("creator_id")
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return target;
    }

    /**
     * Ritorna una LinkedList di {@link Group} a cui lo user è stato invitato e
     * che non ha ancora accettato
     *
     * @param user
     * @return
     */
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
                                        res.getInt("creator_id")
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

    /**
     * Ritorna una LinkedList di {@link User} visibili nel gruppo (che non sono
     * stati bloccati dall'amministratore)
     *
     * @param group
     * @param creatorId
     * @return
     */
    public LinkedList<User> getUsersForGroupAndVisible(int group, int creatorId) {
        LinkedList<User> u = new LinkedList<>();
        try {
            //togliere il creatore del gruppo dalla richiesta
            String query = "SELECT * FROM (SELECT * FROM \"group\" NATURAL JOIN \"user_group\" WHERE group_id = ? AND visible = TRUE) t NATURAL JOIN \"user\" WHERE user_id != ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, group);
                stm.setInt(2, creatorId);
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

    /**
     * Ritorna una LinkedList di {@link User} bloccati dall'amministratore del
     * gruppo
     *
     * @param group
     * @param creatorId
     * @return
     */
    public LinkedList<User> getUsersForGroupAndNotVisible(int group, int creatorId) {
        LinkedList<User> u = new LinkedList<>();
        try {
            String query = "SELECT * FROM (SELECT * FROM \"group\" NATURAL JOIN \"user_group\" WHERE group_id = ? AND visible = FALSE) t NATURAL JOIN \"user\" WHERE user_id != ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, group);
                stm.setInt(2, creatorId);
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

    /**
     * Ritorna una LinkedList di {@link User} che non appartengono al gruppo
     *
     * @param group
     * @param creatorId
     * @return
     */
    public LinkedList<User> getUsersNotInGroup(int group, int creatorId) {
        LinkedList<User> u = new LinkedList<>();
        try {
            String query = "select * from (select * from \"user_group\" natural join \"group\" where group_id = ?) t natural right outer join \"user\" where t.user_id IS NULL AND user_id != ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, group);
                stm.setInt(2, creatorId);
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

    /**
     * Ritorna il gruppo di cui lo user è il creatore
     *
     * @param user
     * @return
     */
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
                            res.getInt("creator_id")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    /**
     * Assegna un nuovo nome al group
     *
     * @param group Gruppo da aggiornare
     * @param name Nuovo nome
     * @return
     */
    public boolean changeGroupName(int group, String name) {
        try {
            String query = "UPDATE \"group\" SET group_name = ? WHERE group_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setString(1, name);
                stm.setInt(2, group);
                return stm.executeUpdate() == 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Chiude il gruppo (non sara più possibile aggiungere post o file
     *
     * @param group
     * @return
     */
    public boolean closeGroup(Group group) {
        try {
            String query = "UPDATE \"group\" SET group_closed = TRUE WHERE group_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setInt(1, group.getId());
                return stm.executeUpdate() == 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Cambia il flag del gruppo a pubblico o privato
     *
     * @param group
     * @param isPublic
     * @return
     */
    public boolean setPublicFlag(Group group, boolean isPublic) {
        try {
            String query = "UPDATE \"group\" SET group_public = ? WHERE group_id = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setBoolean(1, isPublic);
                stm.setInt(2, group.getId());
                return stm.executeUpdate() == 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Aggiorna i membri del gruppo (invita, aggiunge, toglie)
     *
     * @param group ID del gruppo
     * @param m Mappa che ha come chiave l'ID dell'utente e come valore una
     * array di string che contiene "member", "invisible", "visible" a seconda
     * dell'azione da svolgere
     */
    public void updateGroupMembers(int group, Map<String, String[]> m) {
        try {
            String query = "UPDATE \"user_group\" SET visible = ? WHERE group_id = ? AND user_id = ?";
            PreparedStatement stm = connection.prepareStatement(query);
            query = "INSERT INTO \"user_group\"(group_accepted, group_id, visible, user_id) VALUES(?, ?, ?, ?)";
            PreparedStatement stm2 = connection.prepareStatement(query);
            try {
                for (Map.Entry<String, String[]> entry : m.entrySet()) {
                    String key = entry.getKey();
                    int userId;
                    try {
                        userId = Integer.parseInt(key);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    try {
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
                    } catch (SQLException e) {
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

    /**
     * Crea un nuovo gruppo e aggiunge il creatore al gruppo appena creato
     *
     * @param creator ID dell'utente creatore
     * @param name Nome del gruppo
     * @return ID del nuovo gruppo, 0 se si verifica un errore
     */
    public int createGroup(int creator, String name) {
        int groupId = 0;
        try {
            connection.setAutoCommit(false);
            String query = "INSERT INTO \"group\"(creator_id, group_name) VALUES(?, ?)";
            PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, creator);
            stm.setString(2, name);
            stm.executeUpdate();
            ResultSet rs = stm.getGeneratedKeys();
            if (rs != null) {
                if (rs.next()) {
                    groupId = rs.getInt(1);
                }
            }
            stm.close();
            if (groupId > 0) {
                query = "INSERT INTO \"user_group\"(group_accepted, group_id, visible, user_id) VALUES(TRUE, ?, TRUE, ?)";
                stm = connection.prepareStatement(query);
                stm.setInt(1, groupId);
                stm.setInt(2, creator);
                if (stm.executeUpdate() == 1) {
                    connection.commit();
                } else {
                    connection.rollback();
                    groupId = 0;
                }
                stm.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return groupId;
    }

    /**
     * Aggiunge il testo del post e i dati dei relativi file al database
     *
     * @param groupId ID del gruppo a cui appartiene il post
     * @param userId Utente che scrive il post
     * @param text Testo del post
     * @param multipart
     * @return ID del post appena creato, 0 in caso di errore
     */
    public int addPost(int groupId, int userId, String text, MultipartRequest multipart) {
        int postId = 0;
        Enumeration<String> files = multipart.getFileNames();
        try {
            connection.setAutoCommit(false);
            String query = "INSERT INTO \"post\"(user_id, group_id, post_text) VALUES(?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, userId);
            stm.setInt(2, groupId);
            stm.setString(3, text);
            stm.executeUpdate();
            ResultSet rs = stm.getGeneratedKeys();
            if (rs != null) {
                if (rs.next()) {
                    postId = rs.getInt(1);
                }
            }
            stm.close();
            query = "INSERT INTO \"file\"(post_id, file_name, file_mime, file_size) VALUES(?, ?, ?, ?)";
            stm = connection.prepareStatement(query);
            while (files.hasMoreElements()) {
                String name = files.nextElement();
                System.out.println(name);
                String filename = multipart.getFilesystemName(name);
                System.out.println(filename);
                String type = multipart.getContentType(name);
                System.out.println(type);
                File f = multipart.getFile(name);
                if (f != null) {
                    stm.setInt(1, postId);
                    stm.setString(2, filename);
                    stm.setString(3, type);
                    stm.setLong(4, f.length());
                    if (1 != stm.executeUpdate()) {
                        connection.rollback();
                        postId = 0;
                        break;
                    }
                }
            }
            connection.commit();
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return postId;
    }
    
    /**
     * Accetta l'invito al gruppo
     * @param group ID del gruppo
     * @param user ID dell'utente invitato
     */
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
    
    /**
     * Rifiuta l'invito al gruppo
     * @param group ID del gruppo
     * @param user ID dell'utente invitato
     */
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
    
    /**
     * Ritorna l'utliva volta che l'utente ha visualizzato il quick display sulla home
     * @param user
     * @return 
     */
    public Date getLastQickDisplayTime(int user) {
        Date time = null;
        String query = "SELECT user_last_time FROM \"user\" WHERE user_id = ?";
        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(query);
            try {
                stm.setInt(1, user);
                try (ResultSet res = stm.executeQuery()) {
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
    
    /**
     * Aggiorna data e ora dell'ultimo quick display (quando l'utente visualizza la home)
     * @param user 
     */
    public void updateQuickDisplayTime(int user) {
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
    
    /**
     * Ritorna l'istanza di {@link User}  con l'ID passato come parametro, null se l'ID non esiste
     * @param userId
     * @return 
     */
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
    
    /**
     * Overload del metodo {@link #getUser(java.lang.String) }
     * @param userId
     * @return 
     */
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

    public boolean emailInDatabase(String email) {
        boolean exists = false;
        try {
            String query = "SELECT * FROM \"user\" WHERE user_email = ?";
            try (PreparedStatement stm = connection.prepareStatement(query)) {
                stm.setString(1, email);
                try (ResultSet res = stm.executeQuery()) {
                    if (res.next()) {
                        exists = true;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return exists;
    }
}
