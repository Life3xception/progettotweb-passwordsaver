package passwordsaver.progettotwebpasswordsaver.model;

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

    // TODO: eventually, if useful, add isAdmin parameter to method and use it in method
    public ArrayList<PasswordDB> getAllPasswords(String username) {
        ArrayList<PasswordDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = PasswordDB.loadAllPasswords(username, conn, true);
            LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - getAllPasswords: " + (!ret.isEmpty() ? "Passwords loaded" : "Password not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - getAllPasswords: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "PasswordManagerDB - getAllPasswords: " + ex.getMessage());
        }
        return ret;
    }

    // TODO: eventually, if useful, add isAdmin parameter to method and use it in method
    public ArrayList<DetailedPasswordDB> getAllDetailedPasswords(String username) {
        ArrayList<DetailedPasswordDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedPasswordDB.loadAllPasswords(username, conn, true);
            LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - getAllDetailedPasswords: " + (!ret.isEmpty() ? "Passwords loaded" : "Password not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - getAllDetailedPasswords: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "PasswordManagerDB - getAllDetailedPasswords: " + ex.getMessage());
        }
        return ret;
    }

    // TODO: eventually, if useful, add isAdmin parameter to method and use it in method
    public ArrayList<DetailedPasswordDB> getAllDetailedPasswordsByService(String username, int idService) {
        ArrayList<DetailedPasswordDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedPasswordDB.loadAllPasswordsByService(username, conn, true, idService);
            LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - getAllDetailedPasswordsByService: " + (!ret.isEmpty() ? "Passwords loaded" : "Password not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - getAllDetailedPasswordsByService: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"PasswordManagerDB - getAllDetailedPasswordsByService: " + ex.getMessage());
        }
        return ret;
    }

    // TODO: eventually, if useful, add isAdmin parameter to method and use it in method
    public ArrayList<PasswordDB> getAllStarredPasswords(String username, int limit) {
        ArrayList<PasswordDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = PasswordDB.loadAllStarredPasswords(username, conn, true, limit);
            LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - getAllStarredPasswords: " + (!ret.isEmpty() ? "Starred passwords loaded" : "Starred password not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - getAllStarredPasswords: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"PasswordManagerDB - getAllStarredPasswords: " + ex.getMessage());
        }
        return ret;
    }

    // TODO: eventually, if useful, add isAdmin parameter to method and use it in method
    public ArrayList<DetailedPasswordDB> getAllDetailedStarredPasswords(String username, int limit) {
        ArrayList<DetailedPasswordDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedPasswordDB.loadAllStarredPasswords(username, conn, true, limit);
            LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - getAllDetailedStarredPasswords: " + (!ret.isEmpty() ? "Starred passwords loaded" : "Starred password not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - getAllDetailedStarredPasswords: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"PasswordManagerDB - getAllDetailedStarredPasswords: " + ex.getMessage());
        }
        return ret;
    }

    public PasswordDB getPassword(String username, int idPwd) {
        PasswordDB p = null;

        try (Connection conn = persistence.getConnection()) {
            p = PasswordDB.loadPassword(idPwd, conn, true, false);
            LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - getPassword: " + (p != null ? "Password loaded" : "Password not loaded"), conn);
        } catch (Exception ex) {
            System.out.println("PasswordManagerDB - getPassword: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"PasswordManagerDB - getPassword: " + ex.getMessage());
        }

        return p;
    }

    public DetailedPasswordDB getDetailedPassword(String username, int idPwd) {
        DetailedPasswordDB p = null;

        try (Connection conn = persistence.getConnection()) {
            p = DetailedPasswordDB.loadPassword(idPwd, conn, true);
            LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - getDetailedPassword: " + (p != null ? "Password loaded" : "Password not loaded"), conn);
        } catch (Exception ex) {
            System.out.println("PasswordManagerDB - getDetailedPassword: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"PasswordManagerDB - getDetailedPassword: " + ex.getMessage());
        }

        return p;
    }

    public PasswordDB getDecodedPassword(String username, int idPwd) {
        PasswordDB p = null;

        try (Connection conn = persistence.getConnection()) {
            p = PasswordDB.loadPassword(idPwd, conn, true, true);
            LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - getDecodedPassword: " + (p != null ? "Password decoded" : "Password not decoded"), conn);
        } catch (Exception ex) {
            System.out.println("PasswordManagerDB - getDecodedPassword: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"PasswordManagerDB - getDecodedPassword: " + ex.getMessage());
        }

        return p;
    }

    public boolean userIsOwnerOfPassword(int idPwd, String username) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            int idUser = UserManagerDB.getManager().getUserByUsername(username, true).getIdUser();
            ret = PasswordDB.loadPassword(idPwd, conn, true, false).getIdUser() == idUser;
            LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - userIsOwnerOfPassword: " + (ret ? "User owner of password" : "User not owner of password"), conn);
        } catch (Exception ex) {
            System.out.println("PasswordManagerDB - userIsOwnerOfPassword: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"PasswordManagerDB - userIsOwnerOfPassword: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean passwordExists(String username, int idPwd) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = PasswordDB.loadPassword(idPwd, conn, true, false) != null;
            LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - passwordExists: " + (ret ? "Password exists" : "Password doesn't exist"), conn);
        } catch (Exception ex) {
            System.out.println("PasswordManagerDB - passwordExists: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"PasswordManagerDB - passwordExists: "  + ex.getMessage());
        }

        return ret;
    }

    public int addNewPassword(PasswordDB pwd, String username) {
        int ret = -1;

        try (Connection conn = persistence.getConnection()) {
            ret = pwd.saveAsNew(username, conn);
            LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - addNewPassword: " + (ret != -1 ? "Password added" : "Password not added"), conn);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - addNewPassword: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"PasswordManagerDB - addNewPassword: " + ex.getMessage());
        }

        return ret;
    }

    public boolean updatePassword(PasswordDB pwd, String username) {
        boolean updated = false;

        try (Connection conn = persistence.getConnection()) {
            updated = pwd.saveUpdate(conn, username);
            LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - updatePassword: " + (updated ? "Password updated" : "Password not updated"), conn);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - updatePassword: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"PasswordManagerDB - updatePassword: " + ex.getMessage());
        }

        return updated;
    }

    public boolean deletePassword(String username, int idPwd) {
        boolean deleted = false;

        try (Connection conn = persistence.getConnection()) {
            PasswordDB pwd = PasswordDB.loadPassword(idPwd, conn, true, false);
            if(pwd != null) {
                deleted = pwd.delete(conn);
                LogManagerDB.getManager().addNewLogWithConnection(username, "PasswordManagerDB - deletePassword: " + (deleted ? "Password deleted" : "Password not deleted"), conn);
            }
        } catch (Exception ex) {
            System.out.println("PasswordManagerDB - deletePassword: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username,"PasswordManagerDB - deletePassword: " + ex.getMessage());
        }

        return deleted;
    }
}
