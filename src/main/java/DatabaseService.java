import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    private static final Pattern COMMENT_PATTERN = Pattern.compile("--.*|/\\*(.|[\\r\\n])*?\\*/");

    public void executeScript(String fileName) throws SQLException {
        Connection connection = Database.getDbConnection();
        try (Statement statement = connection.createStatement()) {
            List<String> queryList = parseSQLScript(fileName);
            for (String query : queryList) {
                logger.info("Execute query: {}", query);
                statement.execute(query);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> parseSQLScript(String scriptFilePath) throws IOException {
        List<String> sqlStatements = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(scriptFilePath))) {
            StringBuilder currentStatement = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher commentMatcher = COMMENT_PATTERN.matcher(line);
                line = commentMatcher.replaceAll("");
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                currentStatement.append(line).append(" ");
                if (line.endsWith(";")) {
                    sqlStatements.add(currentStatement.toString());
                    currentStatement.setLength(0);
                }
            }
        } catch (IOException e) {
            throw e;
        }
        return sqlStatements;
    }
}
