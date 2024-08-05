package passwordsaver.progettotwebpasswordsaver.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class PoolingPersistenceManager {

    private static PoolingPersistenceManager instance;

    public static PoolingPersistenceManager getPersistenceManager() {
        if (instance == null) {
            instance = new PoolingPersistenceManager();
        }
        return instance;
    }

    private HikariDataSource dataSource;

    private PoolingPersistenceManager() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/passwordsaver");
            config.setUsername("admin");
            config.setPassword("admin");
            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void terminateDataSource() {
        try {
            this.dataSource.close();
            Enumeration<Driver> en = DriverManager.getDrivers();
            while (en.hasMoreElements()) {
                DriverManager.deregisterDriver(en.nextElement());
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}
