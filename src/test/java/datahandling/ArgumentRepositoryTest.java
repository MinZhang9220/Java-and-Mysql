package datahandling;

import controllers.ArgumentController;
import controllers.DiscourseController;
import models.Argument;
import models.Discourse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steps.TestDatabaseManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentRepositoryTest {

    DiscourseController discourseController;
    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;
    TestDatabaseManager testDatabaseManager = new TestDatabaseManager();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    ArgumentController argumentController;
    ArgumentRepository argumentRepository;


    @BeforeEach
    void setUp() {
        connection = sqLiteJDBC.getConnectionToDatabase("test");
        discourseController = new DiscourseController(true);
        argumentController = new ArgumentController(connection, discourseController);
        argumentRepository = argumentController.getArgumentRepository();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        testDatabaseManager.resetDatabase(connection);
    }

    @Test
    void insertArgument() {
        try{
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ? and rephrasing = ?");
            statement.setInt(1, 5);
            statement.setInt(2, 0);
            statement.setInt(3, 133);
            statement.setString(4, "Kare wa nandemo dekiru");
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(0, count);
            Discourse discourse =  discourseController.getDiscourseRepository().getDiscourseById(5);
            Argument argument = new Argument(discourse);
            argument.setStartIndex(0);
            argument.setEndIndex(133);
            argument.setRephrasing("Kare wa nandemo dekiru");
            argumentRepository.insertArgument(argument);
            statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ? and rephrasing = ?");
            statement.setInt(1, 5);
            statement.setInt(2, 0);
            statement.setInt(3, 133);
            statement.setString(4, "Kare wa nandemo dekiru");
            result = statement.executeQuery();
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getDuplicateArgumentCountWithNoDuplicateArguments() {
        Discourse discourse =  discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(0,argumentRepository.getDuplicateArgumentCount(argument, 0, 133));
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into argument(discourseid,startindex,endindex,rephrasing) values (?,?,?,?)");
            preparedStatement.setInt(1,5);
            preparedStatement.setInt(2,0);
            preparedStatement.setInt(3,133);
            preparedStatement.setString(4,"Kare wa nandemo dekiru");
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            assertEquals(1, count);
        }
        catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(1,argumentRepository.getDuplicateArgumentCount(argument, 0, 133));
    }
}