package passwordsaver.progettotwebpasswordsaver.dbmodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PasswordDB {
    private int idPassword;
    private String password;
    private String email;
    private int idUser;
    private int idService;
    private String passPhrase;
    private boolean validity;

    public PasswordDB(int idPassword, String password, String email, int idUser, int idService, String passPhrase, boolean validity) {
        this.idPassword = idPassword;
        this.password = password;
        this.email = email;
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

    public static ArrayList<PasswordDB> loadAllPasswords(Connection conn, String username) throws SQLException {
        ArrayList<PasswordDB> ret = new ArrayList<>();
        String sql = "SELECT * FROM Passwords WHERE IdUser = ? AND Validity = TRUE";
        int idUser = UserDB.loadUserByUsername(conn, username).getIdUser();

        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, idUser);
        ResultSet rs = st.executeQuery();

        while(rs.next()) {
            PasswordDB pwd = new PasswordDB(
                    rs.getInt("IdPassword"),
                    rs.getString("Password"),
                    rs.getString("Email"),
                    rs.getInt("IdUser"),
                    rs.getInt("IdService"),
                    rs.getString("PassPhrase"),
                    rs.getBoolean("Validity")
            );
            ret.add(pwd);
        }

        return ret;
    }
}
