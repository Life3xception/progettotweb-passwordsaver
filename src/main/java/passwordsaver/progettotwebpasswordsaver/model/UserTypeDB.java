package passwordsaver.progettotwebpasswordsaver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTypeDB {
    private int idUserType;
    private String name;
    private boolean validity;

    public UserTypeDB(int idUserType, String name, boolean validity) {
        this.idUserType = idUserType;
        this.name = name;
        this.validity = validity;
    }

    public int getIdUserType() {
        return idUserType;
    }

    public String getName() {
        return name;
    }

    public boolean getValidity() {
        return validity;
    }

    private static UserTypeDB fromResultSet(ResultSet rs) throws SQLException {
        return new UserTypeDB(
                rs.getInt("IdUserType"),
                rs.getString("Name"),
                rs.getBoolean("Validity")
        );
    }

    public static UserTypeDB loadUserType(int idUserType, Connection conn, boolean validityCheck) throws SQLException {
        UserTypeDB ut = null;
        String sql = "SELECT * FROM UserTypes WHERE IdUserType = ?";
        if(validityCheck)
            sql += " AND Validity = TRUE";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idUserType);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                ut = fromResultSet(rs);
            }
        }

        return ut;
    }
}
