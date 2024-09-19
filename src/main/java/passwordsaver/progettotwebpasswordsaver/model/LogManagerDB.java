package passwordsaver.progettotwebpasswordsaver.model;

import passwordsaver.progettotwebpasswordsaver.db.PoolingPersistenceManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

public class LogManagerDB {
    private static LogManagerDB manager;
    private final PoolingPersistenceManager persistence;

    public LogManagerDB() {
        persistence = PoolingPersistenceManager.getPersistenceManager();
    }

    public static LogManagerDB getManager() {
        if(manager == null) {
            manager = new LogManagerDB();
        }
        return manager;
    }

    public void addNewLog(String username, String message) {
        long time = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(time);

        try (Connection conn = persistence.getConnection()) {
            LogTableDB.log(conn, username, timestamp, message);
        } catch (SQLException ex) {
            System.out.println("LogManagerDB - addNewLogWithConnection: " + ex.getMessage());
        }
    }

    public void addNewLogWithConnection(String username, String message, Connection conn) {
        long time = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(time);

        try {
            LogTableDB.log(conn, username, timestamp, message);
        } catch (SQLException ex) {
            System.out.println("LogManagerDB - addNewLogWithConnection: " + ex.getMessage());
        }
    }
}
