package passwordsaver.progettotwebpasswordsaver.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import passwordsaver.progettotwebpasswordsaver.db.PasswordEncoder;
import passwordsaver.progettotwebpasswordsaver.db.PoolingPersistenceManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PasswordManagerDB {
    private static PasswordManagerDB manager;
    private final PoolingPersistenceManager persistence;
    private final BCryptPasswordEncoder passwordEncoder;

    private PasswordManagerDB() {
        persistence = PoolingPersistenceManager.getPersistenceManager();
        passwordEncoder = PasswordEncoder.getPasswordEncoder();
    }

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

    public PasswordDB getPassword(int idPwd) {
        PasswordDB p = null;

        try {
            p = PasswordDB.loadPassword(idPwd, persistence.getConnection());
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - getPassword: " + ex.getMessage());
        }

        return p;
    }

    public int addNewPassword(PasswordDB pwd, String username) {
        int ret = 0;

        try {
            ret = pwd.saveAsNew(username, persistence.getConnection(), passwordEncoder);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - addPassword: " + ex.getMessage());
        }

        return ret;
    }
}
