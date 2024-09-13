package passwordsaver.progettotwebpasswordsaver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceDB {
    private int idService;
    private String name;
    private int idServiceType;
    private int idUser;
    private boolean validity;

    public ServiceDB(int idService, String name, int idServiceType, int idUser, boolean validity) {
        this.idService = idService;
        this.name = name;
        this.idServiceType = idServiceType;
        this.idUser = idUser;
        this.validity = validity;
    }

    public int getIdService() {
        return idService;
    }

    public String getName() {
        return name;
    }

    public int getIdServiceType() {
        return idServiceType;
    }

    public int getIdUser() {
        return idUser;
    }

    public boolean getValidity() {
        return validity;
    }

    private static ServiceDB fromResultSet(ResultSet rs) throws SQLException {
        return new ServiceDB(
                rs.getInt("IdService"),
                rs.getString("Name"),
                rs.getInt("IdServiceType"),
                rs.getInt("IdUser"),
                rs.getBoolean("Validity")
        );
    }

    public static ArrayList<ServiceDB> loadAllServices(String username, Connection conn, boolean validityCheck) throws SQLException {
        ArrayList<ServiceDB> ret = new ArrayList<>();
        String sql = "SELECT * FROM Services WHERE IdUser = ?";
        if(validityCheck)
            sql += " AND Validity = TRUE";

        sql += "\nORDER BY Name";

        int idUser = UserDB.loadUserByUsername(username, conn, true, true).getIdUser();

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idUser);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                ret.add(fromResultSet(rs));
            }
        }

        return ret;
    }

    public static ArrayList<ServiceDB> loadMostUsedServicesByUser(String username, Connection conn, boolean validityCheck, int limit) throws SQLException {
        ArrayList<ServiceDB> ret = new ArrayList<>();
        String sql = """
                    SELECT S.*
                    FROM Services AS S
                    INNER JOIN (
                    \tSELECT IdService, COUNT(*) AS PwdByService
                    \tFROM Passwords
                    \tWHERE IdUser = ?""";
        if(validityCheck)
            sql += " AND Validity = TRUE\n";

        sql += """                  
            \tGROUP BY IdService
            \tHAVING PwdByService > 0
            ) AS Temp
                ON S.IdService = Temp.IdService""";

        if(validityCheck)
            sql += "\nWHERE Validity = TRUE";

        sql += "\nORDER BY Temp.PwdByService DESC";

        if(limit != 0)
            sql += "\nLIMIT ?";

        int idUser = UserDB.loadUserByUsername(username, conn, true, true).getIdUser();

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idUser);
            if(limit != 0)
                st.setInt(2, limit);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                ret.add(fromResultSet(rs));
            }
        }

        return ret;
    }

    public static ServiceDB loadService(int idService, Connection conn, boolean validityCheck) throws SQLException {
        ServiceDB s = null;
        String sql = "SELECT * FROM Services WHERE IdService = ?";
        if(validityCheck)
            sql += " AND Validity = TRUE";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idService);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                s = fromResultSet(rs);
            }
        }

        return s;
    }

    public static boolean findIfNameExists(String name, int idService, int idUser, Connection conn) throws SQLException {
        boolean ret = false;
        String sql = "SELECT COUNT(*) FROM Services WHERE UPPER(Name) = ? AND IdService <> ? AND IdUser = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, name.toUpperCase());
            st.setInt(2, idService);
            st.setInt(3, idUser);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                ret = rs.getInt(1) > 0;
            }
        }

        return ret;
    }

    public int saveAsNew(String loggedUsername, Connection conn) throws SQLException {
        int ret = -1; // -1 means an error occurred during saving
        String sql = "INSERT INTO Services (Name, IdServiceType, IdUser) VALUES (?, ?, ?)";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            // before adding the parameters, we have to retrieve the idUser from the username
            int idUser = UserManagerDB.getManager().getUserByUsername(loggedUsername, true).getIdUser();

            // now we can add the parameters to the statement
            st.setString(1, name);
            st.setInt(2, idServiceType);
            st.setInt(3, idUser);

            if (st.executeUpdate() > 0) {
                // The ID that was generated is maintained in the server on a per-connection basis.
                // This means that the value returned by the function to a given client is the first
                // AUTO_INCREMENT value generated for most recent statement affecting an AUTO_INCREMENT
                // column by that client.
                // So the value returned by LAST_INSERT_ID() is per user and is unaffected by other
                // queries that might be running on the server from other users.
                ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID()");
                if (rs.next()) {
                    idService = rs.getInt(1);
                    ret = idService;
                }
            }
        }

        return ret;
    }

    public boolean saveUpdate(Connection conn) throws SQLException {
        boolean ret = false;
        String sql = "UPDATE Services SET Name = ?, IdServiceType = ? WHERE IdService = ? AND Validity = TRUE";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, name);
            st.setInt(2, idServiceType);
            st.setInt(3, idService);
            ret = st.executeUpdate() > 0;
        }

        return ret;
    }

    public boolean delete(Connection conn) throws SQLException {
        boolean ret = false;
        String sql = "UPDATE Services SET Validity = FALSE WHERE IdService = ? AND Validity = TRUE";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idService);
            ret = st.executeUpdate() > 0;
        }

        return ret;
    }
}
