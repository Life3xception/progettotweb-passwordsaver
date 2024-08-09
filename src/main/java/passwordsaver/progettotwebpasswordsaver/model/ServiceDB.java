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
}
