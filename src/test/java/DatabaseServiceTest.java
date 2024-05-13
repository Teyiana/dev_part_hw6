import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

public class DatabaseServiceTest {
    private DatabaseService databaseService = spy(new DatabaseService(){});

    @Test
    public  void executeScriptTest() throws SQLException, IOException {
        Database db = mock(Database.class);
        setField(Database.class, "INSTANCE", null, db);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        when(db.getConnection()).thenReturn(connection);
        String scriptName = " uy";
        String query = "q";

        doReturn(Collections.singletonList(query)).when(databaseService).parseSQLScript(scriptName);

        databaseService.executeScript(scriptName);

        verify(statement, times(1)).execute(query);
    }

    @Test
    public void testParseSQLScript() throws IOException {
        String fileName = "src/test/resources/parseSqlScriptTest.sql";
        String q0 = "INSERT INTO client (NAME) VALUES('Henry'), ('Nora'); ";
        String q1 = "INSERT INTO project (ID, NAME, CLIENT_ID, START_DATE, FINISH_DATE)" +
                " VALUES ('11', 'WebTech Solutions', '6', '2017-05-17', '2018-04-04')," +
                " ('12', 'Clear Air', '4', '2019-03-12', '2021-04-05'); ";
        String q2 = "SELECT NAME,  MONTH_COUNT FROM ( SELECT NAME, DATEDIFF(mm, START_DATE, FINISH_DATE)" +
                " as MONTH_COUNT FROM PROJECT ) WHERE MONTH_COUNT = (SELECT DATEDIFF(mm, START_DATE, FINISH_DATE)" +
                "  as COUNT  FROM PROJECT ORDER BY COUNT  DESC LIMIT 1) ORDER BY MONTH_COUNT DESC; ";
        String q3 = "DROP TABLE project; ";

        List<String> result = databaseService.parseSQLScript(fileName);

        assertEquals(4, result.size());
        assertEquals(q0, result.get(0));
        assertEquals(q1, result.get(1));
        assertEquals(q2, result.get(2));
        assertEquals(q3, result.get(3));
    }

    private <I,V> void setField(Class<?> _class, String fieldName, I instance, V value) {
        try {
            Field field = _class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
