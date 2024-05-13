import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class E2ETest {

    @Test
    public void endToEndTest() throws IOException, SQLException {
        try {
            DataBaseCleanTableService cleanTableService = new DataBaseCleanTableService();
            cleanTableService.dropTables();
        } catch (SQLException sqlException) {
            //do nothing, exception expected if tables not exists
        }
        DatabaseInitService initService = new DatabaseInitService();
        initService.initDb();

        DatabasePopulateService populateService = new DatabasePopulateService();
        populateService.populateDb();

        DatabaseQueryService queryService = new DatabaseQueryService();

        List<MaxProjectClient> maxProjectClients = queryService.findMaxProjectsClient();

        assertEquals(2, maxProjectClients.size());
        assertEquals("Camila", maxProjectClients.get(0).getName());


        List<LongestProject> findLongestProjects = queryService.findLongestProjects();

        assertEquals(1, findLongestProjects.size());
        assertEquals("Pure Plants", findLongestProjects.get(0).getName());


        List<MaxSalaryWorker> findMaxSalaryWorker = queryService.findMaxSalaryWorker();
        assertEquals(1, findMaxSalaryWorker.size());
        assertEquals("Elizabeth", findMaxSalaryWorker.get(0).getName());

        List<YoungestEldestWorker> findYoungestEldestWorker = queryService.findYoungestEldestWorker();
        assertEquals(2, findYoungestEldestWorker.size());
        assertEquals("Victoria", findYoungestEldestWorker.get(0).getName());

        List<PrintProjectPrices> printProjectPrices = queryService.printProjectPrices();
        assertEquals(10, printProjectPrices.size());
        assertEquals("InnoData IT Experts", printProjectPrices.get(0).getName());

    }
}
