package passwordsaver.progettotwebpasswordsaver.model;

import passwordsaver.progettotwebpasswordsaver.db.PoolingPersistenceManager;

import java.sql.Connection;
import java.sql.SQLException;

public class ServiceManagerDB {
    private static ServiceManagerDB manager;
    private final PoolingPersistenceManager persistence;

    private ServiceManagerDB() { persistence = PoolingPersistenceManager.getPersistenceManager(); }

    public static ServiceManagerDB getManager() {
        if(manager == null) {
            manager = new ServiceManagerDB();
        }
        return manager;
    }

    public ServiceDB getService(int idService) {
        ServiceDB s = null;

        try (Connection conn = persistence.getConnection()) {
            s = ServiceDB.loadService(idService, conn, true);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getService: "  + ex.getMessage());
        }

        return s;
    }

    public boolean userIsOwnerOfService(int idService, String username) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            int idUser = UserManagerDB.getManager().getUserByUsername(username).getIdUser();
            ret = ServiceDB.loadService(idService, conn, true).getIdUser() == idUser;
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - userIsOwnerOfService: "  + ex.getMessage());
        }

        return ret;
    }
}
