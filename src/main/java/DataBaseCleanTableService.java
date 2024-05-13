import java.sql.SQLException;

public class DataBaseCleanTableService extends DatabaseService {
    public static final String DROP_TABLE = "sql/drop_table_db.sql";
    public static final String CLEAN_TABLE = "sql/clean_table_db.sql";

    public static void main(String[] args) throws SQLException {
        DataBaseCleanTableService dataBaseCleanTableService = new DataBaseCleanTableService();
        dataBaseCleanTableService.dropTables();
        dataBaseCleanTableService.cleanTables();
    }

    public void dropTables() throws SQLException {
        executeScript(DROP_TABLE);
    }

    public void cleanTables() throws SQLException {
        executeScript(CLEAN_TABLE);
    }
}
