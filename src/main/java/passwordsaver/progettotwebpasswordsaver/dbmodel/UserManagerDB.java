package passwordsaver.progettotwebpasswordsaver.dbmodel;

import passwordsaver.progettotwebpasswordsaver.db.PoolingPersistenceManager;

import java.sql.Connection;
import java.sql.SQLException;

public class UserManagerDB {
    private static UserManagerDB manager;
    private final PoolingPersistenceManager persistence;

    private UserManagerDB() { persistence = PoolingPersistenceManager.getPersistenceManager(); }

    public static UserManagerDB getManager() {
        if(manager == null) {
            manager = new UserManagerDB();
        }
        return manager;
    }

    public UserDB getUserByUsername(String username) {
        try {
            Connection conn = persistence.getConnection();
            return UserDB.loadUserByUsername(conn, username);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getUserByUsername: " + ex.getMessage());
        }
        return null;
    }
}
