package datahandling;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ActorRepositoryTest {

    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;

    @Before
    void setUp() {
        connection = sqLiteJDBC.getConnectionToDatabase("test");
    }

    @After
    void tearDown() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}