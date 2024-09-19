package passwordsaver.progettotwebpasswordsaver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetailedUserDB {
    private int idUser;
    private String username;
    private String email;
    private String password;
    private int idUserType;
    private String userTypeName;
    private String encodedSecretKey;
    private String initializationVector;
    private boolean validity;

    public DetailedUserDB(int idUser, String username, String email, String password, int idUserType, String userTypeName,
                          String encodedSecretKey, String initializationVector, boolean validity) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.password = password;
        this.idUserType = idUserType;
        this.userTypeName = userTypeName;
        this.encodedSecretKey = encodedSecretKey;
        this.initializationVector = initializationVector;
        this.validity = validity;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getIdUserType() {
        return idUserType;
    }

    public String getUserTypeName() { return userTypeName; }

    public String getEncodedSecretKey() {
        return encodedSecretKey;
    }

    public String getInitializationVector() {
        return initializationVector;
    }

    public boolean getValidity() {
        return validity;
    }

    private static DetailedUserDB fromResultSet(ResultSet rs, boolean limited) throws SQLException {
        return new DetailedUserDB(
                rs.getInt("IdUser"),
                rs.getString("Username"),
                rs.getString("Email"),
                limited ? "" : rs.getString("Password"),
                rs.getInt("IdUserType"),
                rs.getString("UserTypeName"),
                limited ? "" : rs.getString("EncodedSecretKey"),
                limited ? "" : rs.getString("InitializationVector"),
                rs.getBoolean("Validity"));
    }

    public static ArrayList<DetailedUserDB> loadAllUsers(Connection conn, boolean validityCheck) throws SQLException {
        ArrayList<DetailedUserDB> ret = new ArrayList<>();
        String sql = """
            SELECT U.IdUser, U.Username, U.Email, U.IdUserType, U.Validity, UT.Name AS UserTypeName
            FROM Users AS U INNER JOIN UserTypes AS UT
            \tON U.IdUserType = UT.IdUserType""";
        if(validityCheck)
            sql += "\nWHERE Validity = TRUE";

        sql += "\nORDER BY UserTypeName, Username";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                ret.add(fromResultSet(rs, true));
            }
        }
        return ret;
    }

    public static ArrayList<DetailedUserDB> loadAllUsersByUserType(Connection conn, boolean validityCheck, int idUserType) throws SQLException {
        ArrayList<DetailedUserDB> ret = new ArrayList<>();
        String sql = """
            SELECT U.IdUser, U.Username, U.Email, U.IdUserType, U.Validity, UT.Name AS UserTypeName
            FROM Users AS U INNER JOIN UserTypes AS UT
            \tON U.IdUserType = UT.IdUserType
            WHERE U.IdUserType = ?""";
        if(validityCheck)
            sql += " AND Validity = TRUE";

        sql += "\nORDER BY UserTypeName, Username";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idUserType);
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                ret.add(fromResultSet(rs, true));
            }
        }
        return ret;
    }

    public static DetailedUserDB loadUser(int id, Connection conn, boolean validityCheck, boolean limited) throws SQLException {
        DetailedUserDB u = null;
        String sql = "";

        if(limited)
            sql = "SELECT U.IdUser, U.Username, U.Email, U.IdUserType, U.Validity, UT.Name AS UserTypeName";
        else
            sql = "SELECT U.*, UT.Name AS UserTypeName";

        sql += """
            \nFROM Users AS U INNER JOIN UserTypes AS UT
            \tON U.IdUserType = UT.IdUserType
            WHERE U.IdUser = ?""";

        if(validityCheck)
            sql += " AND Validity = TRUE";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                u = fromResultSet(rs, limited);
            }
        }
        return u;
    }

//    public static DetailedUserDB loadUserByUsername(String username, Connection conn, boolean validityCheck, boolean limited) throws SQLException {
//        DetailedUserDB u = null;
//        String sql = "";
//
//        if(limited)
//            sql = "SELECT IdUser, Username, Email, IdUserType, Validity FROM Users WHERE Username = ?";
//        else
//            sql = "SELECT * FROM Users WHERE Username = ?";
//
//        if(validityCheck)
//            sql += " AND Validity = TRUE";
//
//        try(PreparedStatement st = conn.prepareStatement(sql)) {
//            st.setString(1, username);
//            ResultSet rs = st.executeQuery();
//
//            if (rs.next()) {
//                u = fromResultSet(rs, limited);
//            }
//        }
//
//        return u;
//    }
//
//    // The validityCheck tells if the load must be done with the validity=TRUE check or not
//    public static DetailedUserDB loadUserByEmail(String email, Connection conn, boolean validityCheck) throws SQLException {
//        DetailedUserDB u = null;
//        String sql = "SELECT IdUser, Username, Email, IdUserType, Validity FROM Users WHERE Email = ?";
//        if(validityCheck)
//            sql += " AND Validity = TRUE";
//
//        try(PreparedStatement st = conn.prepareStatement(sql)) {
//            st.setString(1, email);
//            ResultSet rs = st.executeQuery();
//
//            if (rs.next()) {
//                u = fromResultSet(rs, true);
//            }
//        }
//
//        return u;
//    }
}
