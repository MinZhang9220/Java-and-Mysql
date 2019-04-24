package controllers;

import datahandling.DiscourseRepository;
import datahandling.SQLiteJDBC;
import models.Argument;
import models.Discourse;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steps.TestDatabaseManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentControllerTest {

    DiscourseController discourseController;
    Scanner scanner;
    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;
    TestDatabaseManager testDatabaseManager = new TestDatabaseManager();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
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
    void createArgumentWithValidDiscourseAndValidStartEndIndicesFromStartOfTextToEndOfText() {
        final InputStream original = System.in;
        String input = "5\n0\n133\nKare wa nandemo dekiru";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        argumentController.createArgument(scanner);
        System.setIn(original);
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

    @Test
    void createArgumentWithValidDiscourseAndValidStartEndIndicesInMiddleOfText() {
        final InputStream original = System.in;
        String input = "5\n50\n80\nKare wa nandemo dekiru";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        argumentController.createArgument(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ? and rephrasing = ?");
            statement.setInt(1, 5);
            statement.setInt(2, 50);
            statement.setInt(3, 80);
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

    @Test
    void createArgumentWithExistingIdenticalArgument() {
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
        final InputStream original = System.in;
        String input = "5\n0\n133\nRephrasingwhichdoesntmatter";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try{
        argumentController.createArgument(scanner);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ? and rephrasing = ?");
            statement.setInt(1, 5);
            statement.setInt(2, 0);
            statement.setInt(3, 136);
            statement.setString(4, "Rephrasingwhichdoesntmatter");
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
    }

    @Test
    void createArgumentWithExistingIdenticalSentenceThenWithUniqueSentence() {
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
        final InputStream original = System.in;
        String input = "5\n0\n133\n50\n80\nRephrasingWhichMatters";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        argumentController.createArgument(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ? and rephrasing = ?");
            statement.setInt(1, 5);
            statement.setInt(2, 50);
            statement.setInt(3, 80);
            statement.setString(4, "RephrasingWhichMatters");
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

    @Test
    void createArgumentWithExistingIdenticalArgumentWithWhiteSpacesAfterSentenceEnd() {
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
        final InputStream original = System.in;
        String input = "5\n0\n136\nRephrasingwhichdoesntmatter";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try{
            argumentController.createArgument(scanner);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ? and rephrasing = ?");
            statement.setInt(1, 5);
            statement.setInt(2, 0);
            statement.setInt(3, 136);
            statement.setString(4, "Rephrasingwhichdoesntmatter");
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(0, count);
            statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ?");
            statement.setInt(1, 5);
            statement.setInt(2, 0);
            statement.setInt(3, 133);
            result = statement.executeQuery();
            count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(1, count);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    void createArgumentWithEndIndexOnWhiteSpace() {
        final InputStream original = System.in;
        String input = "5\n0\n136\nRephrasingwhichdoesntmatter";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        argumentController.createArgument(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ?");
            statement.setInt(1, 5);
            statement.setInt(2, 0);
            statement.setInt(3, 136);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(0, count);
            statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ? and rephrasing = ?");
            statement.setInt(1, 5);
            statement.setInt(2, 0);
            statement.setInt(3, 133);
            statement.setString(4, "Rephrasingwhichdoesntmatter");
            result = statement.executeQuery();
            count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(1, count);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    void createArgumentWithStartIndexOnWhiteSpace() {
        final InputStream original = System.in;
        String input = "4\n0\n112\nRephrasingwhichdoesntmatter";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        argumentController.createArgument(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ?");
            statement.setInt(1, 4);
            statement.setInt(2, 3);
            statement.setInt(3, 112);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(0, count);
            statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ? and rephrasing = ?");
            statement.setInt(1, 4);
            statement.setInt(2, 0);
            statement.setInt(3, 112);
            statement.setString(4, "Rephrasingwhichdoesntmatter");
            result = statement.executeQuery();
            count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(1, count);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    void createArgumentWithEmptyRephrasing() {
        final InputStream original = System.in;
        String input = "4\n0\n112\n\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try{
        argumentController.createArgument(scanner);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ?");
            statement.setInt(1, 4);
            statement.setInt(2, 0);
            statement.setInt(3, 112);
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
    }

    @Test
    void createArgumentWithEmptyRephrasingThenValidRephrashing() {
        final InputStream original = System.in;
        String input = "4\n0\n112\n\nDeku";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try{
            argumentController.createArgument(scanner);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ? and rephrasing = ?");
            statement.setInt(1, 4);
            statement.setInt(2, 0);
            statement.setInt(3, 112);
            statement.setString(4, "Deku");
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

    @Test
    void createArgumentWithNoDiscourses() {
        DiscourseRepository discourseRepository = discourseController.getDiscourseRepository();
        List<Discourse> discourseList = new ArrayList<>();
        discourseRepository.setDiscourses(discourseList);
        final InputStream original = System.in;
        String input = "4\n0\n112\n\nDeku";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try{
            argumentController.createArgument(scanner);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where " +
                    "discourseid = ? and startindex = ? and endindex = ? and rephrasing = ?");
            statement.setInt(1, 4);
            statement.setInt(2, 0);
            statement.setInt(3, 112);
            statement.setString(4, "Deku");
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
    }

    @Test
    void getValidStartEndIndicesWithValidStartEndIndices() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(4);
        Argument argument = new Argument(discourse);
        final InputStream original = System.in;
        String input = "0\n112\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<Integer> startEndIndices = argumentController.getValidStartEndIndices(scanner, argument);
        System.setIn(original);
        assertEquals(0, startEndIndices.get(0));
        assertEquals(112, startEndIndices.get(1));
    }

    @Test
    void getValidStartEndIndicesWithInvalidStartIndex() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(4);
        Argument argument = new Argument(discourse);
        final InputStream original = System.in;
        String input = "5\n112\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<Integer> startEndIndices = null;
        try{
        startEndIndices = argumentController.getValidStartEndIndices(scanner, argument);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        assertNull(startEndIndices);
    }

    @Test
    void getValidStartEndIndicesWithInvalidStartIndexThenValidStartIndex() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(4);
        Argument argument = new Argument(discourse);
        final InputStream original = System.in;
        String input = "5\n112\n0\n112\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<Integer> startEndIndices = argumentController.getValidStartEndIndices(scanner, argument);
        System.setIn(original);
        assertEquals(0, startEndIndices.get(0));
        assertEquals(112, startEndIndices.get(1));
    }

    @Test
    void getValidStartEndIndicesWithInvalidEndIndex() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        final InputStream original = System.in;
        String input = "0\n130\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<Integer> startEndIndices = null;
        try{
            startEndIndices = argumentController.getValidStartEndIndices(scanner, argument);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        assertNull(startEndIndices);
    }

    @Test
    void getValidStartEndIndicesWithInvalidEndIndexThenValidEndIndex() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        final InputStream original = System.in;
        String input = "0\n130\n0\n133\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<Integer> startEndIndices = argumentController.getValidStartEndIndices(scanner, argument);
        System.setIn(original);
        assertEquals(0, startEndIndices.get(0));
        assertEquals(133, startEndIndices.get(1));
    }

    @Test
    void getValidStartEndIndicesWithOutOfBoundsEndIndex() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        final InputStream original = System.in;
        String input = "0\n140\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<Integer> startEndIndices = null;
        try{
            startEndIndices = argumentController.getValidStartEndIndices(scanner, argument);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        assertNull(startEndIndices);
    }

    @Test
    void getValidStartEndIndicesWithOutOfBoundsStartIndex() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        final InputStream original = System.in;
        String input = "-5\n133\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<Integer> startEndIndices = null;
        try{
            startEndIndices = argumentController.getValidStartEndIndices(scanner, argument);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        assertNull(startEndIndices);
    }

    @Test
    void getValidStartEndIndicesWithStartIndexInWhiteSpace() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(4);
        Argument argument = new Argument(discourse);
        final InputStream original = System.in;
        String input = "3\n112\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<Integer> startEndIndices = argumentController.getValidStartEndIndices(scanner, argument);
        System.setIn(original);
        assertEquals(0, startEndIndices.get(0));
        assertEquals(112, startEndIndices.get(1));
    }

    @Test
    void getValidStartEndIndicesWithEndIndexInWhiteSpace() {
        Discourse discourse = discourseController.getDiscourseRepository().getDiscourseById(5);
        Argument argument = new Argument(discourse);
        final InputStream original = System.in;
        String input = "0\n136\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<Integer> startEndIndices = argumentController.getValidStartEndIndices(scanner, argument);
        System.setIn(original);
        assertEquals(0, startEndIndices.get(0));
        assertEquals(133, startEndIndices.get(1));
    }


}