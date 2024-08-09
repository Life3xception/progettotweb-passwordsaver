package passwordsaver.progettotwebpasswordsaver.model;

import passwordsaver.progettotwebpasswordsaver.db.PoolingPersistenceManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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

    /***** SERVICE METHODS *****/

    public ArrayList<ServiceDB> getAllServices(String username) {
        ArrayList<ServiceDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = ServiceDB.loadAllServices(username, conn, true);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getAllServices: " + ex.getMessage());
        }
        return ret;
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
            int idUser = UserManagerDB.getManager().getUserByUsername(username, true).getIdUser();
            ret = ServiceDB.loadService(idService, conn, true).getIdUser() == idUser;
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - userIsOwnerOfService: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean serviceExists(int idService) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = ServiceDB.loadService(idService, conn, true) != null;
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - serviceExists: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean checkIfServiceNameExists(String name, int idService, String username) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            int idUser = UserManagerDB.getManager().getUserByUsername(username, true).getIdUser();
            ret = ServiceDB.findIfNameExists(name, idService, idUser, conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - checkIfServiceNameExists: "  + ex.getMessage());
        }

        return ret;
    }

    public int addNewService(ServiceDB service, String username) {
        int ret = -1;

        try (Connection conn = persistence.getConnection()) {
            ret = service.saveAsNew(username, conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - addNewService: " + ex.getMessage());
        }

        return ret;
    }

    public boolean updateService(ServiceDB service) {
        boolean updated = false;

        try (Connection conn = persistence.getConnection()) {
            updated = service.saveUpdate(conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - updateService: " + ex.getMessage());
        }

        return updated;
    }

    /***** SERVICETYPES METHODS *****/

    public ServiceTypeDB getServiceType(int idServiceType) {
        ServiceTypeDB st = null;

        try (Connection conn = persistence.getConnection()) {
            st = ServiceTypeDB.loadServiceType(idServiceType, conn, true);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getServiceType: " + ex.getMessage());
        }

        return st;
    }
}
