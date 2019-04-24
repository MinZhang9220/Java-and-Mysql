package models;

import controllers.ArgumentController;
import controllers.DiscourseController;
import datahandling.SQLiteJDBC;
import org.junit.Assert;
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

class ArgumentTest {

    DiscourseController discourseController;
    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;
    TestDatabaseManager testDatabaseManager = new TestDatabaseManager();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    ArgumentController argumentController;


    @BeforeEach
    void setUp() {
        connection = sqLiteJDBC.getConnectionToDatabase("test");
        discourseController = new DiscourseController(true);
        argumentController = new ArgumentController(connection, discourseController);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        testDatabaseManager.resetDatabase(connection);
    }

    @Test
    void isValidStartIndexWithStartOfText() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(0, argument.isValidStartIndex(0));
    }

    /**
     * The text begins with three whitespace characters. This asserts that the starting position is shifted to the left.
     */
    @Test
    void isValidStartIndexWithWhiteSpace() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(4);
        Argument argument = new Argument(discourse);
        assertEquals(0, argument.isValidStartIndex(3));
    }

    @Test
    void isValidStartIndexWithMiddleOfText() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(50, argument.isValidStartIndex(50));
    }


    /**
     * Assert that the index is also being shifted to the left when it's on whitespace in the middle of the text
     */
    @Test
    void isValidStartIndexWithMiddleOfTextWithWhiteSpace() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(50, argument.isValidStartIndex(51));
    }

    /**
     * The start index is invalid if the character before it isn't a sentence ending punctuation mark (. or ! or ?)
     */
    @Test
    void isValidStartIndexWithInvalidStartIndex() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(-1, argument.isValidStartIndex(46));
    }

    /**
     * The discourse content would be too short if the start index were to be any higher than this
     */
    @Test
    void isValidStartIndexGreaterThanDiscourseLengthMinusOne() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(-1, argument.isValidStartIndex(135));
    }

    @Test
    void isValidStartIndexGreaterThanDiscourseLessThanZero() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(-1, argument.isValidStartIndex(-5));
    }

    @Test
    void isValidEndIndexWithEndOfText() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(4);
        Argument argument = new Argument(discourse);
        assertEquals(112, argument.isValidEndIndex(112));
    }

    /**
     * The text ends with three whitespace characters. This asserts that the end index is shifted to the left.
     */
    @Test
    void isValiEndIndexWithWhiteSpace() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(133, argument.isValidEndIndex(136));
    }

    @Test
    void isValidEndIndexWithMiddleOfText() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(49, argument.isValidEndIndex(49));
    }

    /**
     * Assert that the index is also being shifted to the left when it's on whitespace in the middle of the text
     */
    @Test
    void isValidEndIndexWithMiddleOfTextWithWhiteSpace() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(49, argument.isValidEndIndex(50));
    }

    /**
     * The end index is invalid if the character before it isn't a sentence ending punctuation mark (. or ! or ?)
     */
    @Test
    void isValidEndIndexWithInvalidEndIndex() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(-1, argument.isValidEndIndex(46));
    }

    /**
     * The end indices cannot overshort the discourse's length
     */
    @Test
    void isValidEndIndexGreaterThanDiscourseLength() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(-1, argument.isValidEndIndex(137));
    }

    @Test
    void isValidEndIndexGreaterThanDiscourseLessThanZero() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        assertEquals(-1, argument.isValidEndIndex(-5));
    }


    @Test
    void hasNoDuplicatesWithADuplicateArgument() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into argument(discourseid,startindex,endindex,rephrasing) values (?,?,?,?)");
            preparedStatement.setInt(1,5);
            preparedStatement.setInt(2,0);
            preparedStatement.setInt(3,133);
            preparedStatement.setString(4,"Kare wa nandemo dekiru");
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            assertEquals(1, count);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
        Argument argument = new Argument(discourseController.getDiscourseRepository().getDiscourseById(5));
        argument.setStartIndex(0);
        argument.setEndIndex(133);
        argument.setRephrasing("This doesn't matter");
        assertFalse(argument.hasNoDuplicates(0,133,argumentController.getArgumentRepository()));
    }

    @Test
    void hasNoDuplicatesWithNoDuplicateArgument() {
        Argument argument = new Argument(discourseController.getDiscourseRepository().getDiscourseById(5));
        argument.setStartIndex(0);
        argument.setEndIndex(133);
        argument.setRephrasing("This doesn't matter");
        assertTrue(argument.hasNoDuplicates(0,133,argumentController.getArgumentRepository()));
    }

    @Test
    void isValidRephrasingWithValidRephrasing() {
        String rephrasing = "A";
        Argument argument = new Argument(discourseController.getDiscourseRepository().getDiscourseById(5));
        assertTrue(argument.isValidRephrasing(rephrasing));
    }

    @Test
    void isValidRephrasingWithNoAlphabetLetter() {
        String rephrasing = ".";
        Argument argument = new Argument(discourseController.getDiscourseRepository().getDiscourseById(5));
        assertFalse(argument.isValidRephrasing(rephrasing));
    }

    @Test
    void isValidRephrasingWithEmptyRephrasing() {
        String rephrasing = "";
        Argument argument = new Argument(discourseController.getDiscourseRepository().getDiscourseById(5));
        assertFalse(argument.isValidRephrasing(rephrasing));
    }

    @Test
    void insertArgumentIntoDatabase() {
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Argument argument = new Argument(discourseController.getDiscourseRepository().getDiscourseById(5));
        argument.setStartIndex(0);
        argument.setEndIndex(133);
        argument.setRephrasing("Kare wa nandemo dekiru");
        assertTrue(argument.insertArgumentIntoDatabase(argumentController.getArgumentRepository()));
        try {
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
            assertEquals(1, count);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}