package passwordsaver.progettotwebpasswordsaver.model;

import passwordsaver.progettotwebpasswordsaver.encryption.AesEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

public class DetailedPasswordDB {
    private int idPassword;
    private String name;
    private String password;
    private String email;
    private String username;
    private int idUser;
    private int idService;
    private String serviceName;
    private String passPhrase;
    private boolean isStarred;
    private boolean validity;

    public DetailedPasswordDB(int idPassword, String name, String password, String email, String username, int idUser, int idService, String serviceName, String passPhrase, boolean isStarred, boolean validity) {
        this.idPassword = idPassword;
        this.name = name;
        this.password = password;
        this.email = email;
        this.username = username;
        this.idUser = idUser;
        this.idService = idService;
        this.serviceName = serviceName;
        this.passPhrase = passPhrase;
        this.isStarred = isStarred;
        this.validity = validity;
    }

    public int getIdPassword() {
        return idPassword;
    }

    public String getName() { return name; }

    public String getPassword() { return password; }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdService() {
        return idService;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getPassPhrase() {
        return passPhrase;
    }

    public boolean getIsStarred() {
        return isStarred;
    }

    public boolean getValidity() { return validity; }

    private static DetailedPasswordDB fromResultSet(ResultSet rs) throws SQLException {
        return new DetailedPasswordDB(
                rs.getInt("IdPassword"),
                rs.getString("Name"),
                rs.getString("Password"),
                rs.getString("Email"),
                rs.getString("Username"),
                rs.getInt("IdUser"),
                rs.getInt("IdService"),
                rs.getString("ServiceName"),
                rs.getString("PassPhrase"),
                rs.getBoolean("IsStarred"), rs.getBoolean("Validity"));
    }

    public static ArrayList<DetailedPasswordDB> loadAllPasswords(String username, Connection conn, boolean validityCheck) throws SQLException {
        ArrayList<DetailedPasswordDB> ret = new ArrayList<>();
        String sql = """
                SELECT P.*, S.Name AS ServiceName
                FROM Passwords AS P INNER JOIN Services AS S ON P.IdService = S.IdService
                WHERE P.IdUser = ?
            """;
        if(validityCheck)
            sql += " AND P.Validity = TRUE AND S.Validity = TRUE";

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

    public static ArrayList<DetailedPasswordDB> loadAllPasswordsByService(String username, Connection conn, boolean validityCheck, int idService) throws SQLException {
        ArrayList<DetailedPasswordDB> ret = new ArrayList<>();
        String sql = """
                SELECT P.*, S.Name AS ServiceName
                FROM Passwords AS P INNER JOIN Services AS S ON P.IdService = S.IdService
                WHERE P.IdUser = ? AND P.IdService = ?
            """;
        if(validityCheck)
            sql += " AND P.Validity = TRUE AND S.Validity = TRUE";

        int idUser = UserDB.loadUserByUsername(username, conn, true, true).getIdUser();

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idUser);
            st.setInt(2, idService);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                ret.add(fromResultSet(rs));
            }
        }

        return ret;
    }

    public static ArrayList<DetailedPasswordDB> loadAllStarredPasswords(String username, Connection conn, boolean validityCheck, int limit) throws SQLException {
        ArrayList<DetailedPasswordDB> ret = new ArrayList<>();
        String sql = """
                SELECT P.*, S.Name AS ServiceName
                FROM Passwords AS P INNER JOIN Services AS S ON P.IdService = S.IdService
                WHERE P.IsStarred = TRUE AND P.IdUser = ?
            """;
        if(validityCheck)
            sql += " AND P.Validity = TRUE AND S.Validity = TRUE";

        if(limit != 0)
            sql += " ORDER BY P.IdPassword LIMIT ?";

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

    public static DetailedPasswordDB loadPassword(int idPwd, Connection conn, boolean validityCheck) throws SQLException {
        DetailedPasswordDB p = null;
        String sql = """
                SELECT P.*, S.Name AS ServiceName
                FROM Passwords AS P INNER JOIN Services AS S ON P.IdService = S.IdService
                WHERE P.IdPassword = ?
            """;
        if(validityCheck)
                sql += " AND P.Validity = TRUE AND S.Validity = TRUE";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idPwd);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                p = fromResultSet(rs);
            }
        }

        return p;
    }
}
