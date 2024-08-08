package passwordsaver.progettotwebpasswordsaver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public static ServiceDB loadService(int idService, Connection conn) throws SQLException {
        ServiceDB s = null;
        String sql = "SELECT * FROM Services WHERE IdService = ? AND Validity = TRUE";
        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idService);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                s = new ServiceDB(
                        rs.getInt("IdService"),
                        rs.getString("Name"),
                        rs.getInt("IdServiceType"),
                        rs.getInt("IdUser"),
                        rs.getBoolean("Validity")
                );
            }
        }

        return s;
    }
}
