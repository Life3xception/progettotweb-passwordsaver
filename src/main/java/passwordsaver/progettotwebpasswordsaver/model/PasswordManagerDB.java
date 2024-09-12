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
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - getAllPasswords: " + ex.getMessage());
        }
        return ret;
    }

    // TODO: eventually, if useful, add isAdmin parameter to method and use it in method
    public ArrayList<DetailedPasswordDB> getAllDetailedPasswords(String username) {
        ArrayList<DetailedPasswordDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedPasswordDB.loadAllPasswords(username, conn, true);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - getAllDetailedPasswords: " + ex.getMessage());
        }
        return ret;
    }

    // TODO: eventually, if useful, add isAdmin parameter to method and use it in method
    public ArrayList<DetailedPasswordDB> getAllDetailedPasswordsByService(String username, int idService) {
        ArrayList<DetailedPasswordDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedPasswordDB.loadAllPasswordsByService(username, conn, true, idService);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - getAllDetailedPasswordsByService: " + ex.getMessage());
        }
        return ret;
    }

    // TODO: eventually, if useful, add isAdmin parameter to method and use it in method
    public ArrayList<PasswordDB> getAllStarredPasswords(String username, int limit) {
        ArrayList<PasswordDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = PasswordDB.loadAllStarredPasswords(username, conn, true, limit);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - getAllStarredPasswords: " + ex.getMessage());
        }
        return ret;
    }

    // TODO: eventually, if useful, add isAdmin parameter to method and use it in method
    public ArrayList<DetailedPasswordDB> getAllDetailedStarredPasswords(String username, int limit) {
        ArrayList<DetailedPasswordDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedPasswordDB.loadAllStarredPasswords(username, conn, true, limit);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - getAllDetailedStarredPasswords: " + ex.getMessage());
        }
        return ret;
    }

    public PasswordDB getPassword(int idPwd) {
        PasswordDB p = null;

        try (Connection conn = persistence.getConnection()) {
            p = PasswordDB.loadPassword(idPwd, conn, true, false);
        } catch (Exception ex) {
            System.out.println("PasswordManagerDB - getPassword: " + ex.getMessage());
        }

        return p;
    }

    public DetailedPasswordDB getDetailedPassword(int idPwd) {
        DetailedPasswordDB p = null;

        try (Connection conn = persistence.getConnection()) {
            p = DetailedPasswordDB.loadPassword(idPwd, conn, true);
        } catch (Exception ex) {
            System.out.println("PasswordManagerDB - getDetailedPassword: " + ex.getMessage());
        }

        return p;
    }

    public PasswordDB getDecodedPassword(int idPwd) {
        PasswordDB p = null;

        try (Connection conn = persistence.getConnection()) {
            p = PasswordDB.loadPassword(idPwd, conn, true, true);
        } catch (Exception ex) {
            System.out.println("PasswordManagerDB - getDecodedPassword: " + ex.getMessage());
        }

        return p;
    }

    public boolean userIsOwnerOfPassword(int idPwd, String username) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            int idUser = UserManagerDB.getManager().getUserByUsername(username, true).getIdUser();
            ret = PasswordDB.loadPassword(idPwd, conn, true, false).getIdUser() == idUser;
        } catch (Exception ex) {
            System.out.println("PasswordManagerDB - userIsOwnerOfPassword: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean passwordExists(int idPwd) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = PasswordDB.loadPassword(idPwd, conn, true, false) != null;
        } catch (Exception ex) {
            System.out.println("PasswordManagerDB - passwordExists: "  + ex.getMessage());
        }

        return ret;
    }

    public int addNewPassword(PasswordDB pwd, String username) {
        int ret = -1;

        try (Connection conn = persistence.getConnection()) {
            ret = pwd.saveAsNew(username, conn);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - addNewPassword: " + ex.getMessage());
        }

        return ret;
    }

    public boolean updatePassword(PasswordDB pwd, String username) {
        boolean updated = false;

        try (Connection conn = persistence.getConnection()) {
            updated = pwd.saveUpdate(conn, username);
        } catch (SQLException ex) {
            System.out.println("PasswordManagerDB - updatePassword: " + ex.getMessage());
        }

        return updated;
    }

    public boolean deletePassword(int idPwd) {
        boolean deleted = false;

        try (Connection conn = persistence.getConnection()) {
            PasswordDB pwd = PasswordDB.loadPassword(idPwd, conn, true, false);
            if(pwd != null)
                deleted = pwd.delete(conn);
        } catch (Exception ex) {
            System.out.println("PasswordManagerDB - deletePassword: " + ex.getMessage());
        }

        return deleted;
    }
}
