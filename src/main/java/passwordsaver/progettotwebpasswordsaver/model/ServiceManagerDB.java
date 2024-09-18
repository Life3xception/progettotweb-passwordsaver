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

    public ArrayList<DetailedServiceDB> getAllDetailedServices(String username, boolean isAdmin) {
        ArrayList<DetailedServiceDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedServiceDB.loadAllServices(username, conn, !isAdmin);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getAllDetailedServices: " + ex.getMessage());
        }
        return ret;
    }

    public ArrayList<DetailedServiceDB> getAllDetailedServicesByServiceType(String username, int idServiceType) {
        ArrayList<DetailedServiceDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedServiceDB.loadAllServicesByServiceType(username, conn, true, idServiceType);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getAllDetailedServicesByServiceType: " + ex.getMessage());
        }
        return ret;
    }

    public ArrayList<ServiceDB> getMostUsedServicesByUser(String username, int limit) {
        ArrayList<ServiceDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = ServiceDB.loadMostUsedServicesByUser(username, conn, true, limit);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getMostUsedServicesByUser: " + ex.getMessage());
        }
        return ret;
    }

    public ServiceDB getService(int idService, boolean isAdmin) {
        ServiceDB s = null;

        try (Connection conn = persistence.getConnection()) {
            s = ServiceDB.loadService(idService, conn, !isAdmin);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getService: "  + ex.getMessage());
        }

        return s;
    }

    public DetailedServiceDB getDetailedService(int idService, boolean isAdmin) {
        DetailedServiceDB s = null;

        try (Connection conn = persistence.getConnection()) {
            s = DetailedServiceDB.loadService(idService, conn, !isAdmin);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getDetailedService: "  + ex.getMessage());
        }

        return s;
    }

    public boolean userIsOwnerOfService(int idService, String username, boolean isAdmin) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            int idUser = UserManagerDB.getManager().getUserByUsername(username, true).getIdUser();
            ret = ServiceDB.loadService(idService, conn, !isAdmin).getIdUser() == idUser;
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - userIsOwnerOfService: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean serviceExists(int idService, boolean isAdmin) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = ServiceDB.loadService(idService, conn, !isAdmin) != null;
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

    public int addNewService(ServiceDB service, String username, boolean isAdmin) {
        int ret = -1;

        try (Connection conn = persistence.getConnection()) {
            ret = service.saveAsNew(username, conn, isAdmin);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - addNewService: " + ex.getMessage());
        }

        return ret;
    }

    public boolean updateService(ServiceDB service, boolean isAdmin) {
        boolean updated = false;

        try (Connection conn = persistence.getConnection()) {
            updated = service.saveUpdate(conn, isAdmin);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - updateService: " + ex.getMessage());
        }

        return updated;
    }

    public boolean deleteService(int idService) {
        boolean deleted = false;

        try (Connection conn = persistence.getConnection()) {
            ServiceDB service = ServiceDB.loadService(idService, conn, true);
            if(service != null)
                deleted = service.delete(conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - updateService: " + ex.getMessage());
        }

        return deleted;
    }

    /***** SERVICETYPES METHODS *****/

    public ArrayList<ServiceTypeDB> getAllServiceTypes(boolean isAdmin) {
        ArrayList<ServiceTypeDB> sts = new ArrayList<>();

        try (Connection conn = persistence.getConnection()) {
            sts = ServiceTypeDB.loadAllServiceTypes(conn, !isAdmin); // se admin -> non mettiamo controllo validità
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getAllServiceTypes: " + ex.getMessage());
        }

        return sts;
    }

    public ServiceTypeDB getServiceType(int idServiceType, boolean isAdmin) {
        ServiceTypeDB st = null;

        try (Connection conn = persistence.getConnection()) {
            st = ServiceTypeDB.loadServiceType(idServiceType, conn, !isAdmin);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getServiceType: " + ex.getMessage());
        }

        return st;
    }

    public boolean checkIfServiceTypeExists(int idServiceType, boolean isAdmin) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = ServiceTypeDB.loadServiceType(idServiceType, conn, !isAdmin) != null;
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - checkIfServiceTypeExists: " + ex.getMessage());
        }

        return ret;
    }

    public boolean checkIfServiceTypeNameExists(String name, int idServiceType) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = ServiceTypeDB.findIfNameExists(name, idServiceType, conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - checkIfServiceTypeNameExists: "  + ex.getMessage());
        }

        return ret;
    }

    public int addNewServiceType(ServiceTypeDB st) {
        int ret = -1;

        try (Connection conn = persistence.getConnection()) {
            ret = st.saveAsNew(conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - addNewServiceType: " + ex.getMessage());
        }

        return ret;
    }

    public boolean updateServiceType(ServiceTypeDB st, boolean isAdmin) {
        boolean updated = false;

        try (Connection conn = persistence.getConnection()) {
            updated = st.saveUpdate(conn, isAdmin);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - updateServiceType: " + ex.getMessage());
        }

        return updated;
    }

    public boolean deleteServiceType(int idServiceType) { // FIXME: qui non serve il parametro isAdmin, perché cancelliamo solo quelli validi, GIUSTO???
        boolean deleted = false;

        try (Connection conn = persistence.getConnection()) {
            ServiceTypeDB st = ServiceTypeDB.loadServiceType(idServiceType, conn, true);
            if(st != null)
                deleted = st.delete(conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - deleteServiceType: " + ex.getMessage());
        }

        return deleted;
    }
}
