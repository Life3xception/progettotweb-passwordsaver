package passwordsaver.progettotwebpasswordsaver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
