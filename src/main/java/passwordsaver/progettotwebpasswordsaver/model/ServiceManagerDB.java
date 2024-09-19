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
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - getAllServices: " + (!ret.isEmpty()? "Services loaded" : "Services not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getAllServices: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - getAllServices: " + ex.getMessage());
        }
        return ret;
    }

    public ArrayList<DetailedServiceDB> getAllDetailedServices(String username, boolean isAdmin) {
        ArrayList<DetailedServiceDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedServiceDB.loadAllServices(username, conn, !isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - getAllDetailedServices: " + (!ret.isEmpty()? "Services loaded" : "Services not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getAllDetailedServices: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - getAllDetailedServices: " + ex.getMessage());
        }
        return ret;
    }

    public ArrayList<DetailedServiceDB> getAllDetailedServicesByServiceType(String username, int idServiceType) {
        ArrayList<DetailedServiceDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = DetailedServiceDB.loadAllServicesByServiceType(username, conn, true, idServiceType);
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - getAllDetailedServicesByServiceType: " + (!ret.isEmpty()? "Services loaded" : "Services not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getAllDetailedServicesByServiceType: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - getAllDetailedServicesByServiceType: " + ex.getMessage());
        }
        return ret;
    }

    public ArrayList<ServiceDB> getMostUsedServicesByUser(String username, int limit) {
        ArrayList<ServiceDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = ServiceDB.loadMostUsedServicesByUser(username, conn, true, limit);
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - getMostUsedServicesByUser: " + (!ret.isEmpty()? "Services loaded" : "Services not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getMostUsedServicesByUser: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - getMostUsedServicesByUser: " + ex.getMessage());
        }
        return ret;
    }

    public ServiceDB getService(String username, int idService, boolean isAdmin) {
        ServiceDB s = null;

        try (Connection conn = persistence.getConnection()) {
            s = ServiceDB.loadService(idService, conn, !isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - getService: " + (s != null ? "Service loaded" : "Service not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getService: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - getService: "  + ex.getMessage());
        }

        return s;
    }

    public DetailedServiceDB getDetailedService(String username, int idService, boolean isAdmin) {
        DetailedServiceDB s = null;

        try (Connection conn = persistence.getConnection()) {
            s = DetailedServiceDB.loadService(idService, conn, !isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - getDetailedService: " + (s != null ? "Service loaded" : "Service not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getDetailedService: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - getDetailedService: "  + ex.getMessage());
        }

        return s;
    }

    public boolean userIsOwnerOfService(int idService, String username, boolean isAdmin) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            int idUser = UserManagerDB.getManager().getUserByUsername(username, true).getIdUser();
            ret = ServiceDB.loadService(idService, conn, !isAdmin).getIdUser() == idUser;
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - userIsOwnerOfService: " + (ret ? "User is owner of service" : "User is not owner of service"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - userIsOwnerOfService: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - userIsOwnerOfService: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean serviceExists(String username, int idService, boolean isAdmin) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = ServiceDB.loadService(idService, conn, !isAdmin) != null;
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - serviceExists: " + (ret ? "Service exists" : "Service doesn't exist"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - serviceExists: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - serviceExists: "  + ex.getMessage());
        }

        return ret;
    }

    public boolean checkIfServiceNameExists(String name, int idService, String username) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            int idUser = UserManagerDB.getManager().getUserByUsername(username, true).getIdUser();
            ret = ServiceDB.findIfNameExists(name, idService, idUser, conn);
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - checkIfServiceNameExists: " + (ret ? "Service name exists" : "Service name doesn't exist"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - checkIfServiceNameExists: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - checkIfServiceNameExists: "  + ex.getMessage());
        }

        return ret;
    }

    public int addNewService(ServiceDB service, String username, boolean isAdmin) {
        int ret = -1;

        try (Connection conn = persistence.getConnection()) {
            ret = service.saveAsNew(username, conn, isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - addNewService: " + (ret != -1 ? "Service added" : "Service not added"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - addNewService: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - addNewService: " + ex.getMessage());
        }

        return ret;
    }

    public boolean updateService(String username, ServiceDB service, boolean isAdmin) {
        boolean updated = false;

        try (Connection conn = persistence.getConnection()) {
            updated = service.saveUpdate(conn, isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - updateService: " + (updated ? "Service updated" : "Service not updated"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - updateService: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - updateService: " + ex.getMessage());
        }

        return updated;
    }

    public boolean deleteService(String username, int idService) {
        boolean deleted = false;

        try (Connection conn = persistence.getConnection()) {
            ServiceDB service = ServiceDB.loadService(idService, conn, true);
            if(service != null) {
                deleted = service.delete(conn);
                LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - deleteService: " + (deleted ? "Service deleted" : "Service not deleted"), conn);
            }
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - updateService: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - updateService: " + ex.getMessage());
        }

        return deleted;
    }

    /***** SERVICETYPES METHODS *****/

    public ArrayList<ServiceTypeDB> getAllServiceTypes(String username, boolean isAdmin) {
        ArrayList<ServiceTypeDB> sts = new ArrayList<>();

        try (Connection conn = persistence.getConnection()) {
            sts = ServiceTypeDB.loadAllServiceTypes(conn, !isAdmin); // se admin -> non mettiamo controllo validità
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - getAllServiceTypes: " + (!sts.isEmpty() ? "Service Types loaded" : "Service Types not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getAllServiceTypes: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - getAllServiceTypes: " + ex.getMessage());
        }

        return sts;
    }

    public ServiceTypeDB getServiceType(String username, int idServiceType, boolean isAdmin) {
        ServiceTypeDB st = null;

        try (Connection conn = persistence.getConnection()) {
            st = ServiceTypeDB.loadServiceType(idServiceType, conn, !isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - getServiceType: " + (st != null ? "Service Type loaded" : "Service Type not loaded"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - getServiceType: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - getServiceType: " + ex.getMessage());
        }

        return st;
    }

    public boolean checkIfServiceTypeExists(String username, int idServiceType, boolean isAdmin) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = ServiceTypeDB.loadServiceType(idServiceType, conn, !isAdmin) != null;
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - getServiceType: " + (ret ? "Service Type exists" : "Service Type doesn't exist"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - checkIfServiceTypeExists: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - checkIfServiceTypeExists: " + ex.getMessage());
        }

        return ret;
    }

    public boolean checkIfServiceTypeNameExists(String username, String name, int idServiceType) {
        boolean ret = false;

        try (Connection conn = persistence.getConnection()) {
            ret = ServiceTypeDB.findIfNameExists(name, idServiceType, conn);
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - checkIfServiceTypeNameExists: " + (ret ? "Service Type name exists" : "Service Type name doesn't exist"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - checkIfServiceTypeNameExists: "  + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - checkIfServiceTypeNameExists: "  + ex.getMessage());
        }

        return ret;
    }

    public int addNewServiceType(String username, ServiceTypeDB st) {
        int ret = -1;

        try (Connection conn = persistence.getConnection()) {
            ret = st.saveAsNew(conn);
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - addNewServiceType: " + (ret != -1 ? "Service Type added" : "Service Type not added"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - addNewServiceType: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - addNewServiceType: " + ex.getMessage());
        }

        return ret;
    }

    public boolean updateServiceType(String username, ServiceTypeDB st, boolean isAdmin) {
        boolean updated = false;

        try (Connection conn = persistence.getConnection()) {
            updated = st.saveUpdate(conn, isAdmin);
            LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - updateServiceType: " + (updated ? "Service Type updated" : "Service Type not updated"), conn);
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - updateServiceType: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - updateServiceType: " + ex.getMessage());
        }

        return updated;
    }

    public boolean deleteServiceType(String username, int idServiceType) {
        boolean deleted = false;

        try (Connection conn = persistence.getConnection()) {
            ServiceTypeDB st = ServiceTypeDB.loadServiceType(idServiceType, conn, true);
            if(st != null) {
                deleted = st.delete(conn);
                LogManagerDB.getManager().addNewLogWithConnection(username, "ServiceManagerDB - deleteServiceType: " + (deleted ? "Service Type deleted" : "Service Type not deleted"), conn);
            }
        } catch (SQLException ex) {
            System.out.println("ServiceManagerDB - deleteServiceType: " + ex.getMessage());
            LogManagerDB.getManager().addNewLog(username, "ServiceManagerDB - deleteServiceType: " + ex.getMessage());
        }

        return deleted;
    }
}
