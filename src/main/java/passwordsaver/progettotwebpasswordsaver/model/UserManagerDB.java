package passwordsaver.progettotwebpasswordsaver.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import passwordsaver.progettotwebpasswordsaver.constants.Config;
import passwordsaver.progettotwebpasswordsaver.db.PoolingPersistenceManager;
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

    /***** USERS METHODS *****/
    public ArrayList<UserDB> getAllUsers(boolean isAdmin) {
        ArrayList<UserDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = UserDB.loadAllUsers(conn, !isAdmin);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getAllUsers: " + ex.getMessage());
        }
        return ret;
    }

    public ArrayList<DetailedUserDB> getAllDetailedUsers(boolean isAdmin) {
        ArrayList<DetailedUserDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedUserDB.loadAllUsers(conn, !isAdmin);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getAllDetailedUsers: " + ex.getMessage());
        }
        return ret;
    }

    public ArrayList<DetailedUserDB> getAllDetailedUsersByUserType(int idUserType, boolean isAdmin) {
        ArrayList<DetailedUserDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedUserDB.loadAllUsersByUserType(conn, !isAdmin, idUserType);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getAllDetailedUsersByUserType: " + ex.getMessage());
        }
        return ret;
    }

    public UserDB getUser(int idUser, boolean isAdmin) {
        UserDB u = null;
        // this is the try-with-resources, it allows to autoclose the resources
        // specified in () if they implement the AutoClosable interface
        try (Connection conn = persistence.getConnection()) {
            u = UserDB.loadUser(idUser, conn, !isAdmin, true);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getUser: " + ex.getMessage());
        }
        return u;
    }

    public DetailedUserDB getDetailedUser(int idUser, boolean isAdmin) {
        DetailedUserDB u = null;
        // this is the try-with-resources, it allows to autoclose the resources
        // specified in () if they implement the AutoClosable interface
        try (Connection conn = persistence.getConnection()) {
            u = DetailedUserDB.loadUser(idUser, conn, !isAdmin, true);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getDetailedUser: " + ex.getMessage());
        }
        return u;
    }

    public UserDB getUserByUsername(String username, boolean limited) {
        UserDB u = null;
        try (Connection conn = persistence.getConnection()) {
            u = UserDB.loadUserByUsername(username, conn, true, limited);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getUserByUsername: " + ex.getMessage());
        }
        return u;
    }

    public UserTypeDB getUserTypeOfUser(String username) {
        UserTypeDB res = null;

        try (Connection conn = persistence.getConnection()) {
            UserDB u = UserDB.loadUserByUsername(username, conn, true, true);
            res = UserTypeDB.loadUserType(u.getIdUserType(), conn, true);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getUserTypeOfUser: "  + ex.getMessage());
        }

        return res;
    }

    public boolean userExists(int idUser, boolean isAdmin) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = UserDB.loadUser(idUser, conn, !isAdmin, true) != null;
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - userExists: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean checkIfUserIsAdmin(String username) {
        boolean isAdmin = false;

        try (Connection conn = persistence.getConnection()) {
            UserDB u = UserDB.loadUserByUsername(username, conn, true, true);
            isAdmin = u.getIdUserType() == Config.adminIdUserType;
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - userExists: "  + ex.getMessage());
        }

        return isAdmin;
    }

    public boolean checkIfEmailExists(int idUser, String email) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            UserDB user = UserDB.loadUserByEmail(email, conn, false);
            // returns true only if the user exists and is not the one provided
            ret = user != null && user.getIdUser() != idUser;
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - checkIfEmailExists: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean checkIfUsernameExists(int idUser, String username) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            UserDB user = UserDB.loadUserByUsername(username, conn, false, true);
            // returns true only if the user exists and is not the one provided
            ret = user != null && user.getIdUser() != idUser;
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - checkIfUserExists: "  + ex.getMessage());
        }

        return ret;
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

    public boolean updateUser(UserDB user, boolean isAdmin) {
        boolean updated = false;

        try (Connection conn = persistence.getConnection()) {
            updated = user.saveUpdate(conn, bcryptEncoder, isAdmin);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - updateUser: " + ex.getMessage());
        }

        return updated;
    }

    public boolean deleteUser(int idUser) {
        boolean deleted = false;

        try (Connection conn = persistence.getConnection()) {
            UserDB user = UserDB.loadUser(idUser, conn, true, true);
            if(user != null)
                deleted = user.delete(conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - deleteUser: " + ex.getMessage());
        }

        return deleted;
    }

    /***** USERTYPES METHODS *****/
    public ArrayList<UserTypeDB> getAllUserTypes() {
        ArrayList<UserTypeDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = UserTypeDB.loadAllUserTypes(conn, true);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getAllUserTypes: " + ex.getMessage());
        }
        return ret;
    }

    public UserTypeDB getUserType(int idUserType) {
        UserTypeDB ut = null;

        try (Connection conn = persistence.getConnection()) {
            ut = UserTypeDB.loadUserType(idUserType, conn, true);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getUserType: " + ex.getMessage());
        }

        return ut;
    }

    public boolean checkIfUserTypeExists(int idUserType) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = UserTypeDB.loadUserType(idUserType, conn, true) != null;
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - checkIfUserTypeExists: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean checkIfUserTypeNameExists(String name, int idUserType) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = UserTypeDB.findIfNameExists(name, idUserType, conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - checkIfUserTypeNameExists: "  + ex.getMessage());
        }

        return ret;
    }

    public int addNewUserType(UserTypeDB ut) {
        int ret = -1;

        try (Connection conn = persistence.getConnection()) {
            ret = ut.saveAsNew(conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - addNewUserType: " + ex.getMessage());
        }

        return ret;
    }

    public boolean updateUserType(UserTypeDB ut) {
        boolean updated = false;

        try (Connection conn = persistence.getConnection()) {
            updated = ut.saveUpdate(conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - updateUserType: " + ex.getMessage());
        }

        return updated;
    }

    public boolean deleteUserType(int idUserType) {
        boolean deleted = false;

        try (Connection conn = persistence.getConnection()) {
            UserTypeDB ut = UserTypeDB.loadUserType(idUserType, conn, true);
            if(ut != null)
                deleted = ut.delete(conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - deleteUserType: " + ex.getMessage());
        }

        return deleted;
    }
}
