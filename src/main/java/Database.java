import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:h2:tcp://localhost:9092/./db/db";
    private static final String DB_USER = "sa";
    private static Database INSTANCE = new Database();
    private HikariConfig config = new HikariConfig();
    private HikariDataSource ds;
    private Database() {
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        ds = new HikariDataSource(config);
    }

    public static Database getInstance() {
        return INSTANCE;
    }

    public static Connection getDbConnection() throws SQLException {
        return getInstance().getConnection();
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
