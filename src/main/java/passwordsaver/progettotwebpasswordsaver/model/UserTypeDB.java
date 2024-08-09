package passwordsaver.progettotwebpasswordsaver.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public static ArrayList<UserTypeDB> loadAllUserTypes(Connection conn, boolean validityCheck) throws SQLException {
        ArrayList<UserTypeDB> ret = new ArrayList<>();
        String sql = "SELECT * FROM UserTypes";
        if(validityCheck)
            sql += " WHERE Validity = TRUE";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                ret.add(fromResultSet(rs));
            }
        }
        return ret;
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

    public static boolean findIfNameExists(String name, int idUserType, Connection conn) throws SQLException {
        boolean ret = false;
        String sql = "SELECT COUNT(*) FROM UserTypes WHERE UPPER(Name) = ? AND IdUserType <> ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, name.toUpperCase());
            st.setInt(2, idUserType);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                ret = rs.getInt(1) > 0;
            }
        }

        return ret;
    }

    public int saveAsNew(Connection conn) throws SQLException {
        int ret = -1;
        String sql = "INSERT INTO UserTypes (Name) VALUES (?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, name);

            if (st.executeUpdate() > 0) {
                ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID()");
                if (rs.next()) {
                    idUserType = rs.getInt(1);
                    ret = idUserType;
                }
            }
        }

        return ret;
    }

    public boolean saveUpdate(Connection conn) throws SQLException {
        boolean ret = false;
        String sql = "UPDATE UserTypes SET Name = ? WHERE IdUserType = ? AND Validity = TRUE";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, name);
            st.setInt(2, idUserType);
            ret = st.executeUpdate() > 0;
        }

        return ret;
    }

    public boolean delete(Connection conn) throws SQLException {
        boolean ret = false;
        String sql = "UPDATE UserTypes SET Validity = FALSE WHERE IdUserType = ? AND Validity = TRUE";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idUserType);
            ret = st.executeUpdate() > 0;
        }

        return ret;
    }
}
