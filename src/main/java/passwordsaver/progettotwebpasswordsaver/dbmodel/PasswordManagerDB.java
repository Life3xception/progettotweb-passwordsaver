package passwordsaver.progettotwebpasswordsaver.dbmodel;

import passwordsaver.progettotwebpasswordsaver.db.PoolingPersistenceManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PasswordManagerDB {
    private static PasswordManagerDB manager;
    private final PoolingPersistenceManager persistence;

    private PasswordManagerDB() { persistence = PoolingPersistenceManager.getPersistenceManager(); }

    public static PasswordManagerDB getManager() {
        if(manager == null) {
            manager = new PasswordManagerDB();
        }
        return manager;
    }

    public ArrayList<PasswordDB> getAllPasswords(String username) {
        ArrayList<PasswordDB> ret = new ArrayList<>();
        try {
            Connection conn = persistence.getConnection();
            ret = PasswordDB.loadAllPasswords(conn, username);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - getAllPasswords: " + ex.getMessage());
        }
        return ret;
    }
}
