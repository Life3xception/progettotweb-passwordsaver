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
    public ArrayList<UserDB> getAllUsers(String username, boolean isAdmin) {
        ArrayList<UserDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = UserDB.loadAllUsers(conn, !isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - getAllUsers: " + (!ret.isEmpty() ? "Users loaded" : "Users not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getAllUsers: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - getAllUsers: " + ex.getMessage());
        }
        return ret;
    }

    public ArrayList<DetailedUserDB> getAllDetailedUsers(String username, boolean isAdmin) {
        ArrayList<DetailedUserDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedUserDB.loadAllUsers(conn, !isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - getAllDetailedUsers: " + (!ret.isEmpty() ? "Users loaded" : "Users not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getAllDetailedUsers: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - getAllDetailedUsers: " + ex.getMessage());
        }
        return ret;
    }

    public ArrayList<DetailedUserDB> getAllDetailedUsersByUserType(String username, int idUserType, boolean isAdmin) {
        ArrayList<DetailedUserDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedUserDB.loadAllUsersByUserType(conn, !isAdmin, idUserType);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - getAllDetailedUsersByUserType: " + (!ret.isEmpty() ? "Users loaded" : "Users not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getAllDetailedUsersByUserType: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - getAllDetailedUsersByUserType: " + ex.getMessage());
        }
        return ret;
    }

    public UserDB getUser(String username, int idUser, boolean isAdmin) {
        UserDB u = null;
        // this is the try-with-resources, it allows to autoclose the resources
        // specified in () if they implement the AutoClosable interface
        try (Connection conn = persistence.getConnection()) {
            u = UserDB.loadUser(idUser, conn, !isAdmin, true);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - getUser: " + (u != null ? "User loaded" : "User not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getUser: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - getUser: " + ex.getMessage());
        }
        return u;
    }

    public DetailedUserDB getDetailedUser(String username, int idUser, boolean isAdmin) {
        DetailedUserDB u = null;
        // this is the try-with-resources, it allows to autoclose the resources
        // specified in () if they implement the AutoClosable interface
        try (Connection conn = persistence.getConnection()) {
            u = DetailedUserDB.loadUser(idUser, conn, !isAdmin, true);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - getDetailedUser: " + (u != null ? "User loaded" : "User not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getDetailedUser: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - getDetailedUser: " + ex.getMessage());
        }
        return u;
    }

    public UserDB getUserByUsername(String username, boolean limited) {
        UserDB u = null;
        try (Connection conn = persistence.getConnection()) {
            u = UserDB.loadUserByUsername(username, conn, true, limited);
//            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - getUserByUsername: " + (u != null ? "User loaded" : "User not loaded"), conn);
            // Rimosso questo log perché logga troppo ed esaurisce i pool di connessione al db
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getUserByUsername: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - getUserByUsername: " + ex.getMessage());
        }
        return u;
    }

    public UserTypeDB getUserTypeOfUser(String username) {
        UserTypeDB res = null;

        try (Connection conn = persistence.getConnection()) {
            UserDB u = UserDB.loadUserByUsername(username, conn, true, true);
            res = UserTypeDB.loadUserType(u.getIdUserType(), conn, true);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - getUserTypeOfUser: " + (res != null ? "User Type loaded" : "User Type not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getUserTypeOfUser: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - getUserTypeOfUser: "  + ex.getMessage());
        }

        return res;
    }

    public boolean userExists(String username, int idUser, boolean isAdmin) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = UserDB.loadUser(idUser, conn, !isAdmin, true) != null;
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - userExists: " + (ret ? "User exists" : "User doesn't exist"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - userExists: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - userExists: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean checkIfUserIsAdmin(String username) {
        boolean isAdmin = false;

        try (Connection conn = persistence.getConnection()) {
            UserDB u = UserDB.loadUserByUsername(username, conn, true, true);
            isAdmin = u.getIdUserType() == Config.adminIdUserType;
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - checkIfUserIsAdmin: " + (isAdmin ? "User is admin" : "User is not admin"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - checkIfUserIsAdmin: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - checkIfUserIsAdmin: "  + ex.getMessage());
        }

        return isAdmin;
    }

    public boolean checkIfEmailExists(String username, int idUser, String email) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            UserDB user = UserDB.loadUserByEmail(email, conn, false);
            // returns true only if the user exists and is not the one provided
            ret = user != null && user.getIdUser() != idUser;
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - checkIfEmailExists: " + (ret ? "User email exists" : "User email doesn't exist"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - checkIfEmailExists: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - checkIfEmailExists: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean checkIfUsernameExists(int idUser, String username) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            UserDB user = UserDB.loadUserByUsername(username, conn, false, true);
            // returns true only if the user exists and is not the one provided
            ret = user != null && user.getIdUser() != idUser;
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - checkIfUserExists: " + (ret ? "User username exists" : "User username doesn't exist"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - checkIfUserExists: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - checkIfUserExists: "  + ex.getMessage());
        }

        return ret;
    }

    // Returns the id of the added user
    public int addNewUser(String username, UserDB user, boolean isAdmin) {
        int ret = -1;

        try (Connection conn = persistence.getConnection()) {
            ret = user.saveAsNew(conn, bcryptEncoder, isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - addNewUser: " + (ret != 0 ? "User added" : "User not added"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - addNewUser: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - addNewUser: " + ex.getMessage());
        }

        return ret;
    }

    public boolean updateUser(String username, UserDB user, boolean isAdmin) {
        boolean updated = false;

        try (Connection conn = persistence.getConnection()) {
            updated = user.saveUpdate(conn, bcryptEncoder, isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - updateUser: " + (updated ? "User updated" : "User not updated"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - updateUser: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - updateUser: " + ex.getMessage());
        }

        return updated;
    }

    public boolean changePassword(String username, UserDB user) {
        boolean changed = false;

        try (Connection conn = persistence.getConnection()) {
            changed = user.changePassword(conn, bcryptEncoder);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - changePassword: " + (changed ? "User password changed" : "User password not changed"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - changePassword: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - changePassword: " + ex.getMessage());
        }

        return changed;
    }

    public boolean deleteUser(String username, int idUser) {
        boolean deleted = false;

        try (Connection conn = persistence.getConnection()) {
            UserDB user = UserDB.loadUser(idUser, conn, true, true);
            if(user != null) {
                deleted = user.delete(conn);
                LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - deleteUser: " + (deleted ? "User deleted" : "User not deleted"), conn);
            }
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - deleteUser: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - deleteUser: " + ex.getMessage());
        }

        return deleted;
    }

    /***** USERTYPES METHODS *****/
    public ArrayList<UserTypeDB> getAllUserTypes(String username, boolean isAdmin) {
        ArrayList<UserTypeDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = UserTypeDB.loadAllUserTypes(conn, !isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - getAllUserTypes: " + (!ret.isEmpty() ? "User Types loaded" : "User Types not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getAllUserTypes: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - getAllUserTypes: " + ex.getMessage());
        }
        return ret;
    }

    public UserTypeDB getUserType(String username, int idUserType, boolean isAdmin) {
        UserTypeDB ut = null;

        try (Connection conn = persistence.getConnection()) {
            ut = UserTypeDB.loadUserType(idUserType, conn, !isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - getAllUserTypes: " + (ut != null ? "User Type loaded" : "User Type not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - getUserType: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - getUserType: " + ex.getMessage());
        }

        return ut;
    }

    public boolean checkIfUserTypeExists(String username, int idUserType, boolean isAdmin) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = UserTypeDB.loadUserType(idUserType, conn, !isAdmin) != null;
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - checkIfUserTypeExists: " + (ret ? "User Type exists" : "User Type doesn't exist"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - checkIfUserTypeExists: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - checkIfUserTypeExists: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean checkIfUserTypeNameExists(String username, String name, int idUserType) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = UserTypeDB.findIfNameExists(name, idUserType, conn);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - checkIfUserTypeNameExists: " + (ret ? "User Type name exists" : "User Type name doesn't exist"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - checkIfUserTypeNameExists: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - checkIfUserTypeNameExists: "  + ex.getMessage());
        }

        return ret;
    }

    public int addNewUserType(String username, UserTypeDB ut, boolean isAdmin) {
        int ret = -1;

        try (Connection conn = persistence.getConnection()) {
            ret = ut.saveAsNew(conn, isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - addNewUserType: " + (ret != -1 ? "User Type added" : "User Type not added"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - addNewUserType: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - addNewUserType: " + ex.getMessage());
        }

        return ret;
    }

    public boolean updateUserType(String username, UserTypeDB ut, boolean isAdmin) {
        boolean updated = false;

        try (Connection conn = persistence.getConnection()) {
            updated = ut.saveUpdate(conn, isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - updateUserType: " + (updated ? "User Type updated" : "User Type not updated"), conn);
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - updateUserType: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - updateUserType: " + ex.getMessage());
        }

        return updated;
    }

    public boolean deleteUserType(String username, int idUserType) {
        boolean deleted = false;

        try (Connection conn = persistence.getConnection()) {
            UserTypeDB ut = UserTypeDB.loadUserType(idUserType, conn, true);
            if(ut != null) {
                deleted = ut.delete(conn);
                LogManagerDB.getManager().addNewLogWithConnection(username,"UserManagerDB - deleteUserType: " + (deleted ? "User Type deleted" : "User Type not deleted"), conn);
            }
        } catch (SQLException ex) {
            System.out.println("UserManagerDB - deleteUserType: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"UserManagerDB - deleteUserType: " + ex.getMessage());
        }

        return deleted;
    }
}
