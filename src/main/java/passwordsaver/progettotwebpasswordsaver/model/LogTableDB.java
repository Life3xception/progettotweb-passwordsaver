package passwordsaver.progettotwebpasswordsaver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class LogTableDB {
    private int idLog;
    private int idUser;
    private Timestamp timestamp;
    private String message;

    public LogTableDB(int idLog, int idUser, Timestamp timestamp, String message) {
        this.idLog = idLog;
        this.idUser = idUser;
        this.timestamp = timestamp;
        this.message = message;
    }

    public int getIdLog() {
        return idLog;
    }

    public int getIdUser() {
        return idUser;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public static boolean log(Connection conn, String username, Timestamp timestamp, String message) throws SQLException {
        boolean ret = false;
        String sql = "INSERT INTO LogTable (IdUser, Timestamp, Message) VALUES (?,?,?)";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            UserDB user = UserManagerDB.getManager().getUserByUsername(username, true);
            int idUser = user != null ? user.getIdUser() : 0;

            st.setInt(1, idUser);
            st.setTimestamp(2, timestamp);
            st.setString(3, message);

            ret =  st.executeUpdate() > 0;
        }

        return ret;
    }
}
