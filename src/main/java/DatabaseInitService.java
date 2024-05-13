import java.sql.SQLException;
public class DatabaseInitService extends DatabaseService {
    private static final String INIT_DB = "sql/init_db.sql";

    public static void main(String[] args) throws SQLException {
        DatabaseInitService databaseInitService = new DatabaseInitService();
        databaseInitService.initDb();
    }

    public void initDb() throws SQLException {
        executeScript(INIT_DB);
    }
}
