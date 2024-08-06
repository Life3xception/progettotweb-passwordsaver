package passwordsaver.progettotwebpasswordsaver.dbmodel;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import passwordsaver.progettotwebpasswordsaver.db.PasswordEncoder;
import passwordsaver.progettotwebpasswordsaver.db.PoolingPersistenceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginManagerDB {
    private static LoginManagerDB manager;
    private final PoolingPersistenceManager persistence;
    private final BCryptPasswordEncoder encoder;

    private LoginManagerDB() {
        persistence = PoolingPersistenceManager.getPersistenceManager();
        encoder = PasswordEncoder.getPasswordEncoder();
    }

    public static LoginManagerDB getManager() {
        if(manager == null) {
            manager = new LoginManagerDB();
        }
        return manager;
    }

    public boolean validateCredentials(String username, String password) {
        boolean result = false;

        try {
            // retrieve connection
            Connection conn = persistence.getConnection();
            // preparing of query
            PreparedStatement st = conn.prepareStatement("""
                SELECT * FROM Users
                WHERE Username = ? AND Validity = TRUE
                """);

            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            // only a row should be retrieved
            if(rs.next()) {
                // check if password is correct
                String hashedPwd = rs.getString("Password");
                result = encoder.matches(password, hashedPwd);
            }
        } catch (SQLException ex) {
            // evaluate to log in DB
            System.out.println("LoginManagerDB - validateCredentials: " + ex.getMessage());
        }

        return result;
    }
}
