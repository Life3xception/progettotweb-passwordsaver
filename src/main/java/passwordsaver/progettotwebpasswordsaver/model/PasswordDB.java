package passwordsaver.progettotwebpasswordsaver.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import passwordsaver.progettotwebpasswordsaver.encryption.AesEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

public class PasswordDB {
    private int idPassword;
    private String password;
    private String email;
    private String username;
    private int idUser;
    private int idService;
    private String passPhrase;
    private boolean validity;

    public PasswordDB(int idPassword, String password, String email, String username, int idUser, int idService, String passPhrase, boolean validity) {
        this.idPassword = idPassword;
        this.password = password;
        this.email = email;
        this.username = username;
        this.idUser = idUser;
        this.idService = idService;
        this.passPhrase = passPhrase;
        this.validity = validity;
    }

    public int getIdPassword() {
        return idPassword;
    }

    public String getPassword() {
        return password;
    }

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

    public String getPassPhrase() {
        return passPhrase;
    }

    public boolean getValidity() {
        return validity;
    }

    private static PasswordDB fromResultSet(ResultSet rs) throws SQLException {
        return new PasswordDB(
                rs.getInt("IdPassword"),
                rs.getString("Password"),
                rs.getString("Email"),
                rs.getString("Username"),
                rs.getInt("IdUser"),
                rs.getInt("IdService"),
                rs.getString("PassPhrase"),
                rs.getBoolean("Validity")
        );
    }

    public static ArrayList<PasswordDB> loadAllPasswords(String username, Connection conn) throws SQLException {
        ArrayList<PasswordDB> ret = new ArrayList<>();
        String sql = "SELECT * FROM Passwords WHERE IdUser = ? AND Validity = TRUE";
        int idUser = UserDB.loadUserByUsername(username, conn, true).getIdUser();

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idUser);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                ret.add(fromResultSet(rs));
            }
        }

        return ret;
    }

    public static PasswordDB loadPassword(int idPwd, Connection conn) throws SQLException {
        PasswordDB p = null;
        String sql = "SELECT * FROM Passwords WHERE IdPassword = ? AND Validity = TRUE";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idPwd);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                p = fromResultSet(rs);
            }
        }

        return p;
    }

    public int saveAsNew(String loggedUsername, Connection conn) throws SQLException {
        int ret = -1; // -1 means an error occurred during saving
        String sql = "INSERT INTO Passwords (Password, Email, Username, IdUser, IdService, PassPhrase) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            // before adding the parameters, we have to retrieve the idUser from the username
            // and to encrypt the password! To encrypt the password we have to recover the secretkey and the IV
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(loggedUsername);

            try {
                this.password = Base64.getEncoder().encodeToString(
                        AesEncoder.do_AESEncryption(this.password,
                                AesEncoder.getSecretKeyFromByteArray(Base64.getDecoder().decode(loggedUser.getEncodedSecretKey())),
                                Base64.getDecoder().decode(loggedUser.getInitializationVector())
                        ));
            } catch (Exception ex) {
                System.out.println("PasswordDB - saveAsNew: " + ex.getMessage());

                // we return earlier than usual, so we must close the statement
                // (autoclose will not be performed)
                st.close();

                return ret;
            }

            // now we can add the parameters to the statement
            st.setString(1, password);
            st.setString(2, email);
            st.setString(3, username);
            st.setInt(4, idUser);
            st.setInt(5, idService);
            st.setString(6, passPhrase); // TODO: needs to be validated?

            if (st.executeUpdate() > 0) {
                // The ID that was generated is maintained in the server on a per-connection basis.
                // This means that the value returned by the function to a given client is the first
                // AUTO_INCREMENT value generated for most recent statement affecting an AUTO_INCREMENT
                // column by that client.
                // So the value returned by LAST_INSERT_ID() is per user and is unaffected by other
                // queries that might be running on the server from other users.
                ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID()");
                if (rs.next()) {
                    idPassword = rs.getInt(1);
                    ret = idPassword;
                }
            }
        }

        return ret;
    }

    public boolean saveUpdate(Connection conn, String loggedUsername) throws SQLException {
        boolean ret = false;
        String sql = """
                    UPDATE Passwords SET
                    Password = ?,
                    Email = ?,
                    Username = ?,
                    IdService = ?,
                    PassPhrase = ?
                    WHERE IdPassword = ? AND Validity = TRUE
                """;

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            // before adding the parameters, we have to retrieve the idUser from the username
            // and to encrypt the password! To encrypt the password we have to recover the secretkey
            // and the initialization vector
            UserDB loggedUser = UserManagerDB.getManager().getUserByUsername(loggedUsername);

            try {
                this.password = Base64.getEncoder().encodeToString(
                        AesEncoder.do_AESEncryption(this.password,
                                AesEncoder.getSecretKeyFromByteArray(Base64.getDecoder().decode(loggedUser.getEncodedSecretKey())),
                                Base64.getDecoder().decode(loggedUser.getInitializationVector())
                        ));
            } catch (Exception ex) {
                System.out.println("PasswordDB - saveAsNew: " + ex.getMessage());

                // we return earlier than usual, so we must close the statement
                // (autoclose will not be performed)
                st.close();

                return ret;
            }

            st.setString(1, password);
            st.setString(2, email);
            st.setString(3, username);
            st.setInt(4, idService);
            st.setString(5, passPhrase);
            st.setInt(6, idPassword);
            ret = st.executeUpdate() > 0;
        }
        return ret;
    }

    public boolean delete(Connection conn) throws SQLException {
        boolean deleted = false;
        String sql = "UPDATE Passwords SET Validity = FALSE WHERE IdPassword = ? AND Validity = TRUE";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idPassword);
            deleted = st.executeUpdate() > 0;
        }

        return deleted;
    }
}
