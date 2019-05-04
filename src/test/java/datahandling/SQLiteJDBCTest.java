package datahandling;

import datahandling.SQLiteJDBC;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteJDBCTest {

    @Test
    void connectionToDatabase() {
        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
        assertNotNull(sqLiteJDBC.getConnectionToDatabase("test"));
    }
}