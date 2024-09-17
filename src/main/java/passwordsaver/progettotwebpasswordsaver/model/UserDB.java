package passwordsaver.progettotwebpasswordsaver.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import passwordsaver.progettotwebpasswordsaver.encryption.AesEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

public class UserDB {
    private int idUser;
    private String username;
    private String email;
    private String password;
    private int idUserType;
    private String encodedSecretKey;
    private String initializationVector;
    private boolean validity;

    public UserDB(int idUser, String username, String email, String password, int idUserType,
                  String encodedSecretKey, String initializationVector, boolean validity) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.password = password;
        this.idUserType = idUserType;
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

    public String getEncodedSecretKey() {
        return encodedSecretKey;
    }

    public String getInitializationVector() {
        return initializationVector;
    }

    public boolean getValidity() {
        return validity;
    }

    private static UserDB fromResultSet(ResultSet rs, boolean limited) throws SQLException {
        return new UserDB(
                rs.getInt("IdUser"),
                rs.getString("Username"),
                rs.getString("Email"),
                limited ? "" : rs.getString("Password"),
                rs.getInt("IdUserType"),
                limited ? "" : rs.getString("EncodedSecretKey"),
                limited ? "" : rs.getString("InitializationVector"),
                rs.getBoolean("Validity")
        );
    }

    public static ArrayList<UserDB> loadAllUsers(Connection conn, boolean validityCheck) throws SQLException {
        ArrayList<UserDB> ret = new ArrayList<>();
        String sql = "SELECT IdUser, Username, Email, IdUserType, Validity FROM Users";
        if(validityCheck)
            sql += " WHERE Validity = TRUE";

        sql += "\nORDER BY Username";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                ret.add(fromResultSet(rs, true));
            }
        }
        return ret;
    }

    public static UserDB loadUser(int id, Connection conn, boolean validityCheck, boolean limited) throws SQLException {
        UserDB u = null;
        String sql = "";

        if(limited)
            sql = "SELECT IdUser, Username, Email, IdUserType, Validity FROM Users WHERE IdUser = ?";
        else
            sql = "SELECT * FROM Users WHERE IdUser = ?";

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

    public static UserDB loadUserByUsername(String username, Connection conn, boolean validityCheck, boolean limited) throws SQLException {
        UserDB u = null;
        String sql = "";

        if(limited)
            sql = "SELECT IdUser, Username, Email, IdUserType, Validity FROM Users WHERE Username = ?";
        else
            sql = "SELECT * FROM Users WHERE Username = ?";

        if(validityCheck)
            sql += " AND Validity = TRUE";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                u = fromResultSet(rs, limited);
            }
        }

        return u;
    }

    // The validityCheck tells if the load must be done with the validity=TRUE check or not
    public static UserDB loadUserByEmail(String email, Connection conn, boolean validityCheck) throws SQLException {
        UserDB u = null;
        String sql = "SELECT IdUser, Username, Email, IdUserType, Validity FROM Users WHERE Email = ?";
        if(validityCheck)
            sql += " AND Validity = TRUE";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                u = fromResultSet(rs, true);
            }
        }

        return u;
    }

    public int saveAsNew(Connection conn, BCryptPasswordEncoder encoder) throws SQLException {
        int ret = -1;
        String sql = "INSERT INTO Users (Email, Username, Password, IdUserType, " +
                "EncodedSecretKey, InitializationVector) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            // we suppose to have, at this point, a plaintext password in our property,
            // but we MUST save it as encoded!
            this.password = encoder.encode(this.password);

            // now we can add the parameters to the statement
            st.setString(1, email);
            st.setString(2, username);
            st.setString(3, password);
            st.setInt(4, idUserType);

            try {
                // we need to create secretkey for the user
                encodedSecretKey = Base64.getEncoder().encodeToString(AesEncoder.createAESKey().getEncoded());
                st.setString(5, encodedSecretKey);
                // and also the initialization vector
                initializationVector = Base64.getEncoder().encodeToString(AesEncoder.createInitializationVector());
                st.setString(6, initializationVector);
            } catch (Exception ex) {
                System.out.println("UserDB - saveAsNew: " + ex.getMessage());

                // we return earlier than usual, so we must close the statement
                // (autoclose will not be performed)
                st.close();
                return ret;
            }

            if (st.executeUpdate() > 0) {
                // The ID that was generated is maintained in the server on a per-connection basis.
                // This means that the value returned by the function to a given client is the first
                // AUTO_INCREMENT value generated for most recent statement affecting an AUTO_INCREMENT
                // column by that client.
                // So the value returned by LAST_INSERT_ID() is per user and is unaffected by other
                // queries that might be running on the server from other users.
                ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID()");
                if (rs.next()) {
                    idUser = rs.getInt(1);
                    ret = idUser;
                }
            }
        }
        return ret; // means an error occurred during saving
    }

    public boolean saveUpdate(Connection conn, BCryptPasswordEncoder encoder) throws SQLException {
        boolean ret = false;
        String sql = """
            UPDATE Users SET
            Email = ?,
            Username = ?,
            IdUserType = ?
            WHERE IdUser = ? AND Validity = TRUE
        """;

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            // TODO: cambiamento password va fatto in apposito metodo

            st.setString(1, email);
            st.setString(2, username);
            //st.setInt(3, idUserType); // FIXME: il cambiamento va permesso solo se l'utente è admin!

            /*try {
                // FIXME: il cambiamento va permesso solo se l'utente è admin!
                //  meglio se fatto in un metodo apposito!
                //encodedSecretKey = Base64.getEncoder().encodeToString(AesEncoder.createAESKey().getEncoded());
                //st.setString(4, encodedSecretKey);

                // FIXME: il cambiamento va permesso solo se l'utente è admin!
                //  meglio se fatto in un metodo apposito!
                //initializationVector = Base64.getEncoder().encodeToString(AesEncoder.createInitializationVector());
                //st.setString(5, initializationVector);
            } catch (Exception ex) {
                System.out.println("UserDB - saveUpdate: " + ex.getMessage());
                return false;
            }*/

            st.setInt(4, idUser);
            ret = st.executeUpdate() > 0;
        }

        return ret;
    }

    public boolean delete(Connection conn) throws SQLException {
        boolean deleted = false;
        String sql = "UPDATE Users SET Validity = FALSE WHERE IdUser = ? AND Validity = TRUE";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idUser);
            deleted = st.executeUpdate() > 0;
        }

        return deleted;
    }
}
