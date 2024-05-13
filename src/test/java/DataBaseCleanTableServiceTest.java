import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DataBaseCleanTableServiceTest {
    private  DataBaseCleanTableService dataBaseCleanTableService = spy(new DataBaseCleanTableService());

    @Test
    public void dropTablesTest() throws SQLException {
        doNothing().when(dataBaseCleanTableService).executeScript(anyString());

        dataBaseCleanTableService.dropTables();

        verify(dataBaseCleanTableService, times(1)).executeScript(DataBaseCleanTableService.DROP_TABLE);
    }

    @Test
    public void cleanTablesTest() throws SQLException {
        doNothing().when(dataBaseCleanTableService).executeScript(anyString());

        dataBaseCleanTableService.cleanTables();

        verify(dataBaseCleanTableService, times(1)).executeScript(DataBaseCleanTableService.CLEAN_TABLE);
    }

}
