import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseQueryService extends DatabaseService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseQueryService.class);
    private static final String MAX_PROJECTS_CLIENT = "sql/find_max_projects_client.sql";
    private static final String LONGEST_PROJECTS = "sql/find_longest_project.sql";
    private static final String MAX_SALARY_WORKER = "sql/find_max_salary_worker.sql";
    private static final String YOUNGEST_ELDEST_WORKER = "sql/find_youngest_eldest_workers.sql";
    private static final String PRINT_PROJECT_PRICES = "sql/print_project_prices.sql";
    private Gson GSON = new Gson();

    public List<MaxProjectClient> findMaxProjectsClient() throws SQLException, IOException {
        List<String> queryList = parseSQLScript(MAX_PROJECTS_CLIENT);
        if (queryList.size() == 1) {
            List<MaxProjectClient> result = executeQuery(queryList.get(0), MaxProjectClient.class);
            return result;
        }
        throw new IllegalStateException("expected only one resultSet for one select in " + MAX_PROJECTS_CLIENT);
    }

    public List<LongestProject> findLongestProjects() throws SQLException, IOException {
        List<String> queryList = parseSQLScript(LONGEST_PROJECTS);
        if (queryList.size() == 1) {
            List<LongestProject> result = executeQuery(queryList.get(0), LongestProject.class);
            return result;
        }
        throw new IllegalStateException("expected only one resultSet for one select in " + LONGEST_PROJECTS);
    }

    public List<MaxSalaryWorker> findMaxSalaryWorker() throws SQLException, IOException {
        List<String> queryList = parseSQLScript(MAX_SALARY_WORKER);
        if (queryList.size() == 1) {
            List<MaxSalaryWorker> result = executeQuery(queryList.get(0), MaxSalaryWorker.class);
            return result;
        }
        throw new IllegalStateException("expected only one resultSet for one select in " + MAX_SALARY_WORKER);
    }

    public List<YoungestEldestWorker> findYoungestEldestWorker() throws SQLException, IOException {
        List<String> queryList = parseSQLScript(YOUNGEST_ELDEST_WORKER);
        if (queryList.size() == 1) {
            List<YoungestEldestWorker> result = executeQuery(queryList.get(0), YoungestEldestWorker.class);
            return result;
        }
        throw new IllegalStateException("expected only one resultSet for one select in " + YOUNGEST_ELDEST_WORKER);
    }

    public List<PrintProjectPrices> printProjectPrices() throws SQLException, IOException {
        List<String> queryList = parseSQLScript(PRINT_PROJECT_PRICES);
        if (queryList.size() == 1) {
            List<PrintProjectPrices> result = executeQuery(queryList.get(0), PrintProjectPrices.class);
            return result;
        }
        throw new IllegalStateException("expected only one resultSet for one select in " + PRINT_PROJECT_PRICES);
    }

    private <T> List<T> mapResultSet(ResultSet resultSet, Class<T> resultClass) throws SQLException {
        ResultSetMetaData meta = resultSet.getMetaData();
        Map<String, List<String>> dataMap = new HashMap<>();
        int columnCount = meta.getColumnCount();
        for (int columnNumber = 1; columnNumber <= columnCount; columnNumber++) {
            List<String> dataList = new ArrayList<>();
            dataMap.put(meta.getColumnLabel(columnNumber), dataList);
        }
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
            for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
                entry.getValue().add(resultSet.getString(entry.getKey()));
            }
        }
        List<T> result = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            result.add(parseRow(resultClass, dataMap, rowIndex));
        }
        return result;
    }

    private <T> T parseRow(Class<T> resultClass, Map<String, List<String>> dataMap, int rowIndex) {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
            sb.append('\"')
                    .append(toCamelCase(entry.getKey()))
                    .append("\":\"")
                    .append(entry.getValue().get(rowIndex))
                    .append("\",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return GSON.fromJson(sb.toString(), resultClass);
    }

    private String toCamelCase(String columnName) {
        String[] resHolder = new String[1];
        resHolder[0] = columnName.toLowerCase();
        Pattern pattern = Pattern.compile("(_[a-z])");
        Matcher matcher = pattern.matcher(resHolder[0]);
        matcher.results().forEach(r -> resHolder[0] = resHolder[0].replaceAll(r.group(), toUpperCaseLetter(r)));
        return resHolder[0];
    }

    private String toUpperCaseLetter(MatchResult r) {
        String g = r.group();
        return g.substring(1).toUpperCase();
    }

    public <T> List<T> executeQuery(String query, Class<T> resultClass) throws SQLException {
        Connection connection = Database.getDbConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            logger.info("Execute query: {}", query);
            if (resultSet != null) {
                return mapResultSet(resultSet, resultClass);
            }
            throw new IllegalStateException("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        throw new IllegalStateException("");
    }

}
