package passwordsaver.progettotwebpasswordsaver.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import passwordsaver.progettotwebpasswordsaver.db.PoolingPersistenceManager;
import passwordsaver.progettotwebpasswordsaver.encryption.AesEncoder;
import passwordsaver.progettotwebpasswordsaver.encryption.BcryptEncoder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserManagerDB {
    private static UserManagerDB manager;
    private final PoolingPersistenceManager persistence;
    private final BCryptPasswordEncoder bcryptEncoder;

    private UserManagerDB() {
        persistence = PoolingPersistenceManager.getPersistenceManager();
        bcryptEncoder = BcryptEncoder.getPasswordEncoder();
    }

    public static UserManagerDB getManager() {
        if(manager == null) {
            manager = new UserManagerDB();
        }
        return manager;
    }

    public ArrayList<UserDB> getAllUsers() {
        ArrayList<UserDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = UserDB.loadAllUsers(conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getAllUsers: " + ex.getMessage());
        }
        return ret;
    }

    public UserDB getUser(int idUser) {
        UserDB u = null;
        // this is the try-with-resources, it allows to autoclose the resources
        // specified in () if they implement the AutoClosable interface
        try (Connection conn = persistence.getConnection()) {
            u = UserDB.loadUser(idUser, conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getUser: " + ex.getMessage());
        }
        return u;
    }

    public UserDB getUserByUsername(String username) {
        UserDB u = null;
        try (Connection conn = persistence.getConnection()) {
            u = UserDB.loadUserByUsername(username, conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getUserByUsername: " + ex.getMessage());
        }
        return u;
    }

    // Returns the id of the added user
    public int addNewUser(UserDB user) {
        int ret = -1;

        try (Connection conn = persistence.getConnection()) {
            ret = user.saveAsNew(conn, bcryptEncoder);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - addNewUser: " + ex.getMessage());
        }

        return ret;
    }

    public boolean updateUser(UserDB user) {
        boolean updated = false;

        try (Connection conn = persistence.getConnection()) {
            updated = user.saveUpdate(conn, bcryptEncoder);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - updateUser: " + ex.getMessage());
        }

        return updated;
    }
}
