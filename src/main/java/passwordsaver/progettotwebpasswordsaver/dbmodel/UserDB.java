package passwordsaver.progettotwebpasswordsaver.dbmodel;

import com.mysql.cj.protocol.Resultset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDB {
    private int idUser;
    private String username;
    private String email;
    private String passwordHash;
    private int idUserType;
    private boolean validity;

    public UserDB(int idUser, String username, String email, String passwordHash, int idUserType, boolean validity) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.idUserType = idUserType;
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

    /*public String getPasswordHash() {
        return passwordHash;
    }*/

    public int getIdUserType() {
        return idUserType;
    }

    public boolean isValidity() {
        return validity;
    }

    public static UserDB loadUserByUsername(Connection conn, String username) throws SQLException {
        UserDB u = null;

        String sql = "SELECT * FROM Users WHERE Username = ? AND Validity = TRUE";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, username);
        ResultSet rs = st.executeQuery();

        if(rs.next()) {
            u = new UserDB(
                    rs.getInt("IdUser"),
                    rs.getString("Username"),
                    rs.getString("Email"),
                    rs.getString("Password"),
                    rs.getInt("IdUserType"),
                    rs.getBoolean("Validity")
            );
        }

        return u;
    }
}
