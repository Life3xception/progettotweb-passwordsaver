package passwordsaver.progettotwebpasswordsaver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetailedServiceDB {
    private int idService;
    private String name;
    private int idServiceType;
    private String serviceTypeName;
    private int idUser;
    private boolean validity;

    public DetailedServiceDB(int idService, String name, int idServiceType, String serviceTypeName, int idUser, boolean validity) {
        this.idService = idService;
        this.name = name;
        this.idServiceType = idServiceType;
        this.serviceTypeName = serviceTypeName;
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

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public int getIdUser() {
        return idUser;
    }

    public boolean getValidity() {
        return validity;
    }

    private static DetailedServiceDB fromResultSet(ResultSet rs) throws SQLException {
        return new DetailedServiceDB(
                rs.getInt("IdService"),
                rs.getString("Name"),
                rs.getInt("IdServiceType"),
                rs.getString("ServiceTypeName"),
                rs.getInt("IdUser"),
                rs.getBoolean("Validity"));
    }

    public static ArrayList<DetailedServiceDB> loadAllServices(String username, Connection conn, boolean validityCheck) throws SQLException {
        ArrayList<DetailedServiceDB> ret = new ArrayList<>();
        String sql = """
                SELECT S.*, ST.Name as ServiceTypeName
                FROM Services AS S INNER JOIN ServiceTypes AS ST
                    ON S.IdServiceType = ST.IdServiceType
                WHERE S.IdUser = ?
                """;
        if(validityCheck)
            sql += " AND S.Validity = TRUE AND ST.Validity = TRUE";

        sql += "\nORDER BY S.Name";

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

    public static ArrayList<DetailedServiceDB> loadAllServicesByServiceType(String username, Connection conn, boolean validityCheck, int idServiceType) throws SQLException {
        ArrayList<DetailedServiceDB> ret = new ArrayList<>();
        String sql = """
                SELECT S.*, ST.Name as ServiceTypeName
                FROM Services AS S INNER JOIN ServiceTypes AS ST
                    ON S.IdServiceType = ST.IdServiceType
                WHERE S.IdUser = ? AND S.IdServiceType = ?
                """;
        if(validityCheck)
            sql += " AND S.Validity = TRUE AND ST.Validity = TRUE";

        sql += "\nORDER BY S.Name";

        int idUser = UserDB.loadUserByUsername(username, conn, true, true).getIdUser();

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idUser);
            st.setInt(2, idServiceType);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                ret.add(fromResultSet(rs));
            }
        }

        return ret;
    }

    public static DetailedServiceDB loadService(int idService, Connection conn, boolean validityCheck) throws SQLException {
        DetailedServiceDB s = null;
        String sql = """
            SELECT S.*, ST.Name as ServiceTypeName
            FROM Services AS S INNER JOIN ServiceTypes AS ST
                ON S.IdServiceType = ST.IdServiceType
            WHERE S.IdService = ?
            """;
        if (validityCheck)
            sql += " AND S.Validity = TRUE AND ST.Validity = TRUE";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idService);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                s = fromResultSet(rs);
            }
        }

        return s;
    }
}
