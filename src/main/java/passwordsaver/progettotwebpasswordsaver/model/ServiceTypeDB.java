package passwordsaver.progettotwebpasswordsaver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceTypeDB {
    private int idServiceType;
    private String name;
    private boolean validity;

    public ServiceTypeDB(int idServiceType, String name, boolean validity) {
        this.idServiceType = idServiceType;
        this.name = name;
        this.validity = validity;
    }

    public int getIdServiceType() {
        return idServiceType;
    }

    public String getName() {
        return name;
    }

    public boolean getValidity() {
        return validity;
    }

    private static ServiceTypeDB fromResultSet(ResultSet rs) throws SQLException {
        return new ServiceTypeDB(
                rs.getInt("IdServiceType"),
                rs.getString("Name"),
                rs.getBoolean("Validity")
        );
    }

    public static ArrayList<ServiceTypeDB> loadAllServiceTypes(Connection conn, boolean validityCheck) throws SQLException {
        ArrayList<ServiceTypeDB> serviceTypes = new ArrayList<>();
        String sql = "SELECT * FROM ServiceTypes";
        if(validityCheck)
            sql += " WHERE Validity = TRUE";

        sql += "\nORDER BY Name";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                serviceTypes.add(fromResultSet(rs));
            }
        }

        return serviceTypes;
    }

    public static ServiceTypeDB loadServiceType(int idServiceType, Connection conn, boolean validityCheck) throws SQLException {
        ServiceTypeDB serviceType = null;
        String sql = "SELECT * FROM ServiceTypes WHERE IdServiceType = ?";
        if(validityCheck)
            sql += " AND Validity = TRUE";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idServiceType);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                serviceType = fromResultSet(rs);
            }
        }

        return serviceType;
    }

    public static boolean findIfNameExists(String name, int idServiceType, Connection conn) throws SQLException {
        boolean ret = false;
        String sql = "SELECT COUNT(*) FROM ServiceTypes WHERE UPPER(Name) = ? AND IdServiceType <> ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, name.toUpperCase());
            st.setInt(2, idServiceType);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                ret = rs.getInt(1) > 0;
            }
        }

        return ret;
    }

    public int saveAsNew(Connection conn) throws SQLException {
        int ret = -1;
        String sql = "INSERT INTO ServiceTypes (Name) VALUES (?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, name);

            if (st.executeUpdate() > 0) {
                ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID()");
                if (rs.next()) {
                    idServiceType = rs.getInt(1);
                    ret = idServiceType;
                }
            }
        }

        return ret;
    }

    public boolean saveUpdate(Connection conn) throws SQLException {
        boolean ret = false;
        String sql = "UPDATE ServiceTypes SET Name = ? WHERE IdServiceType = ? AND Validity = TRUE";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, name);
            st.setInt(2, idServiceType);
            ret = st.executeUpdate() > 0;
        }

        return ret;
    }

    public boolean delete(Connection conn) throws SQLException {
        boolean ret = false;
        String sql = "UPDATE ServiceTypes SET Validity = FALSE WHERE IdServiceType = ? AND Validity = TRUE";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idServiceType);
            ret = st.executeUpdate() > 0;
        }

        return ret;
    }
}
