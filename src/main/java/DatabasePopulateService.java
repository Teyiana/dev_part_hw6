import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DatabasePopulateService extends DatabaseService {
    private static final String POPULATE_DB = "sql/populate_db.sql";
    private static final Logger logger = LoggerFactory.getLogger(DatabasePopulateService.class);

    public static void main(String[] args) throws SQLException, IOException {
        DatabasePopulateService databasePopulateService = new DatabasePopulateService();
        databasePopulateService.populateDb();
        DatabaseQueryService databaseQueryService = new DatabaseQueryService();
        List<MaxProjectClient> maxProjectClients = databaseQueryService.findMaxProjectsClient();
        logger.info(maxProjectClients.toString());
        List<LongestProject> findLongestProjects = databaseQueryService.findLongestProjects();
        logger.info(findLongestProjects.toString());
        List<MaxSalaryWorker> findMaxSalaryWorker = databaseQueryService.findMaxSalaryWorker();
        logger.info(findMaxSalaryWorker.toString());
        List<YoungestEldestWorker> findYoungestEldestWorker = databaseQueryService.findYoungestEldestWorker();
        logger.info(findYoungestEldestWorker.toString());
        List<PrintProjectPrices> printProjectPrices = databaseQueryService.printProjectPrices();
        logger.info(printProjectPrices.toString());
    }

    public void populateDb() throws SQLException {
        executeScript(POPULATE_DB);
    }
}
