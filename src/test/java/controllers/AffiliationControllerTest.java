package controllers;

import datahandling.SQLiteJDBC;
import models.Actor;
import models.Affiliation;
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
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class AffiliationControllerTest {

    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;
    TestDatabaseManager testDatabaseManager = new TestDatabaseManager();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    ActorController actorController;
    AffiliationController affiliationController;
    OrganisationController organisationController;
    Scanner scanner;

    @BeforeEach
    void setUp() {
        connection = sqLiteJDBC.getConnectionToDatabase("test");
        testDatabaseManager.populateDatabaseWithActors(connection);
        testDatabaseManager.populateDatabaseWithOrganisations(connection);
        actorController = new ActorController(connection);
        organisationController = new OrganisationController(connection);
        affiliationController = new AffiliationController(connection, actorController, organisationController);
        actorController.setAffiliationController(affiliationController);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        testDatabaseManager.resetDatabase(connection);
    }

    @Test
    void createAffiliationWithValidRoleAndStartDateAndEndDate() {
        final InputStream original = System.in;
        String input = "U.A\n2\nTeacher\n2018-01-15\n2018-12-05";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        affiliationController.createAffiliation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and role = ? and startdate = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "Teacher");
            statement.setString(4, "2018-01-15");
            statement.setString(5, "2018-12-05");
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithStartDatePriorToEndDate() {
        final InputStream original = System.in;
        String input = "U.A\n2\nTeacher\n2018-12-15\n2018-12-05";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try{
        affiliationController.createAffiliation(scanner);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and role = ? and startdate = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "Teacher");
            statement.setString(4, "2018-12-15");
            statement.setString(5, "2018-12-05");
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithStartDatePriorToEndDateThenEndDatePriorToStartDate() {
        final InputStream original = System.in;
        String input = "U.A\n2\nTeacher\n2018-12-15\n2018-12-05\n2018-12-05\n2018-12-15";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        affiliationController.createAffiliation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and role = ? and startdate = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "Teacher");
            statement.setString(4, "2018-12-05");
            statement.setString(5, "2018-12-15");
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithNoStartDate() {
        final InputStream original = System.in;
        String input = "U.A\n2\nTeacher\n\n2018-12-15";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        affiliationController.createAffiliation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from affiliation where actorid = ? and organisationname = ?" +
                            " and role = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "Teacher");
            statement.setString(4, "2018-12-15");
            ResultSet result = statement.executeQuery();
            String startDate = "placeholder";
            while (result.next()) {
                startDate = result.getString("startdate");
            }
            assertNull(startDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithInvalidStartDate() {
        final InputStream original = System.in;
        String input = "U.A\n2\nTeacher\nThisIsInvalid\n2018-12-15";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try{
            affiliationController.createAffiliation(scanner);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and startdate = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "ThisIsInvalid");
            statement.setString(4, "2018-12-15");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    void createAffiliationWithInvalidStartDateThenValidStartDate() {
        final InputStream original = System.in;
        String input = "U.A\n2\nTeacher\nThisIsInvalid\n2018-12-15\n2018-01-05\n2018-12-15";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        affiliationController.createAffiliation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and startdate = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "2018-01-05");
            statement.setString(4, "2018-12-15");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithNoEndDate() {
        final InputStream original = System.in;
        String input = "U.A\n2\nTeacher\n2018-12-05\n\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        affiliationController.createAffiliation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from affiliation where actorid = ? and organisationname = ?" +
                            " and role = ? and startdate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "Teacher");
            statement.setString(4, "2018-12-05");
            ResultSet result = statement.executeQuery();
            String endDate = "placeholder";
            while (result.next()) {
                endDate = result.getString("enddate");
            }
            assertNull(endDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithInvalidEndDate() {
        final InputStream original = System.in;
        String input = "U.A\n2\nTeacher\n2018-01-05\nThisIsInvalid";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try{
            affiliationController.createAffiliation(scanner);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and startdate = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "2018-01-05");
            statement.setString(4, "ThisIsInvalid");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithInvalidEndDateThenValidEndDate() {
        final InputStream original = System.in;
        String input = "U.A\n2\nTeacher\n2018-01-05\nThisIsInvalid\n2018-01-05\n2019-01-05";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        affiliationController.createAffiliation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and startdate = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "2018-01-05");
            statement.setString(4, "2019-01-05");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithNoStartDateAndNoEndDate() {
        final InputStream original = System.in;
        String input = "U.A\n2\nTeacher\n\n\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        affiliationController.createAffiliation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from affiliation where actorid = ? and organisationname = ?" +
                            " and role = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "Teacher");
            ResultSet result = statement.executeQuery();
            String startDate = "placeholder";
            String endDate = "placeholder";
            while (result.next()) {
                startDate = result.getString("startdate");
                endDate = result.getString("enddate");
            }
            assertNull(startDate);
            assertNull(endDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithNoRole() {
        final InputStream original = System.in;
        String input = "U.A\n2\n\n2018-01-05\n2018-12-15";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        affiliationController.createAffiliation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from affiliation where actorid = ? and organisationname = ?" +
                            " and startdate = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "2018-01-05");
            statement.setString(4, "2018-12-15");
            ResultSet result = statement.executeQuery();
            String role = "placeholder";
            while (result.next()) {
                role = result.getString("role");
            }
            assertNull(role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithInvalidRole() {
        final InputStream original = System.in;
        String input = "U.A\n2\nThisRoleIsOverOneHundredCharactersLongXDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
                "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD\n2018-01-05\n2018-12-15";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try{
        affiliationController.createAffiliation(scanner);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and startdate = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "2018-01-05");
            statement.setString(4, "2018-12-15");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithInvalidRoleThenValidRole() {
        final InputStream original = System.in;
        String input = "U.A\n2\nThisRoleIsOverOneHundredCharactersLongXDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
                "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD\nJedi Master\n2018-01-05\n2018-12-15";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        affiliationController.createAffiliation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            "and role = ? and startdate = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "Jedi Master");
            statement.setString(4, "2018-01-05");
            statement.setString(5, "2018-12-15");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithNoOrganisationsInDatabase() {
        try {
            PreparedStatement statement = connection.prepareStatement("delete from organisation");
            statement.closeOnCompletion();
            assertEquals(4,statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final InputStream original = System.in;
        String input = "U.A\n2\nTeacher\n2018-01-15\n2018-12-05";;
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        affiliationController.createAffiliation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and role = ? and startdate = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "Teacher");
            statement.setString(4, "2018-01-15");
            statement.setString(5, "2018-12-05");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAffiliationWithNoActorsInDatabase() {
        try {
            PreparedStatement statement = connection.prepareStatement("delete from actor");
            statement.closeOnCompletion();
            assertEquals(4,statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final InputStream original = System.in;
        String input = "U.A\n2\nTeacher\n2018-01-15\n2018-12-05";;
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        affiliationController.createAffiliation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and role = ? and startdate = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "Teacher");
            statement.setString(4, "2018-01-15");
            statement.setString(5, "2018-12-05");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(0, count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getValidStartEndDateFromUserWithValidStartEndDate() {
        final InputStream original = System.in;
        String input = "2018-01-15\n2018-12-05";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Affiliation affiliation = new Affiliation();
        String[] startEndDates = affiliationController.getValidStartEndDateFromUser(scanner, affiliation);
        System.setIn(original);
        assertEquals("2018-01-15", startEndDates[0]);
        assertEquals("2018-12-05", startEndDates[1]);
    }

    @Test
    void getValidStartEndDateFromUserWithEndDatePriorToStartDate() {
        final InputStream original = System.in;
        String input = "2018-12-15\n2018-12-05";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Affiliation affiliation = new Affiliation();
        String[] startEndDates = null;
        try{
        startEndDates = affiliationController.getValidStartEndDateFromUser(scanner, affiliation);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        assertNull(startEndDates);
    }

    @Test
    void getValidStartEndDateFromUserWithEndDatePriorToStartDateThenStartDatePriorToEndDate() {
        final InputStream original = System.in;
        String input = "2018-12-15\n2018-12-05\n2018-12-05\n2018-12-15";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Affiliation affiliation = new Affiliation();
        String[] startEndDates = affiliationController.getValidStartEndDateFromUser(scanner, affiliation);
        System.setIn(original);
        assertEquals("2018-12-05", startEndDates[0]);
        assertEquals("2018-12-15", startEndDates[1]);
    }

    @Test
    void getValidStartEndDateFromUserWithInvalidStartDate() {
        final InputStream original = System.in;
        String input = "asdasd\n2018-12-05";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Affiliation affiliation = new Affiliation();
        String[] startEndDates = null;
        try{
        startEndDates = affiliationController.getValidStartEndDateFromUser(scanner, affiliation);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        assertNull(startEndDates);
    }

    @Test
    void getValidStartEndDateFromUserWithInvalidStartDateThenValidStartDate() {
        final InputStream original = System.in;
        String input = "asdasd\n2018-12-05\n2018-12-05\n2018-12-15";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Affiliation affiliation = new Affiliation();
        String[] startEndDates = affiliationController.getValidStartEndDateFromUser(scanner, affiliation);
        System.setIn(original);
        assertEquals("2018-12-05", startEndDates[0]);
        assertEquals("2018-12-15", startEndDates[1]);
    }

    @Test
    void getValidStartEndDateFromUserWithInvalidEndDate() {
        final InputStream original = System.in;
        String input = "2017-02-28\nXD";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Affiliation affiliation = new Affiliation();
        String[] startEndDates = null;
        try{
            startEndDates = affiliationController.getValidStartEndDateFromUser(scanner, affiliation);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        assertNull(startEndDates);
    }

    @Test
    void getValidStartEndDateFromUserWithInvalidEndDateThenValidEndDate() {
        final InputStream original = System.in;
        String input = "2017-02-28\nXD\n2017-02-28\n2018-03-25";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Affiliation affiliation = new Affiliation();
        String[] startEndDates = affiliationController.getValidStartEndDateFromUser(scanner, affiliation);
        System.setIn(original);
        assertEquals("2017-02-28", startEndDates[0]);
        assertEquals("2018-03-25", startEndDates[1]);
    }

    @Test
    void getValidStartEndDateFromUserWithNoStartDate() {
        final InputStream original = System.in;
        String input = "\n2018-03-25";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Affiliation affiliation = new Affiliation();
        String[] startEndDates = affiliationController.getValidStartEndDateFromUser(scanner, affiliation);
        System.setIn(original);
        assertEquals("", startEndDates[0]);
        assertEquals("2018-03-25", startEndDates[1]);
    }

    @Test
    void getValidStartEndDateFromUserWithNoEndDate() {
        final InputStream original = System.in;
        String input = "2019-04-20\n\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Affiliation affiliation = new Affiliation();
        String[] startEndDates = affiliationController.getValidStartEndDateFromUser(scanner, affiliation);
        System.setIn(original);
        assertEquals("2019-04-20", startEndDates[0]);
        assertEquals("", startEndDates[1]);
    }

    @Test
    void getValidStartEndDateFromUserWithNoStartDateAndNoEndDate() {
        final InputStream original = System.in;
        String input = "\n\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Affiliation affiliation = new Affiliation();
        String[] startEndDates = affiliationController.getValidStartEndDateFromUser(scanner, affiliation);
        System.setIn(original);
        assertEquals("", startEndDates[0]);
        assertEquals("", startEndDates[1]);
    }

    @Test
    void getAffiliationsByActorWithNoAffiliations() {
        Actor actor = new Actor(1, "Katsuki", "Bakugo", null);
        assertEquals(0,affiliationController.getAffiliationsByActor(actor).size());
    }

    @Test
    void getAffiliationsByActorWithAffiliations() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into affiliation(actorid,organisationname,role,startdate,enddate) values (?,?,?,?,?)");
            preparedStatement.setInt(1,1);
            preparedStatement.setString(2,"U.A");
            preparedStatement.setString(3,"Hero");
            preparedStatement.setString(4,"2018-01-05");
            preparedStatement.setString(5,"2020-12-03");
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            assertEquals(1, count);
            preparedStatement = connection.prepareStatement(
                    "insert into affiliation(actorid,organisationname,role,startdate,enddate) values (?,?,?,?,?)");
            preparedStatement.setInt(1,1);
            preparedStatement.setString(2,"University Of Canterbury");
            preparedStatement.setString(3,"Student");
            preparedStatement.setString(4,"2017-01-05");
            preparedStatement.setString(5,"2020-12-03");
            preparedStatement.closeOnCompletion();
            count = preparedStatement.executeUpdate();
            assertEquals(1, count);
            Actor actor = new Actor(1, "Katsuki", "Bakugo", null);
            assertEquals(2,affiliationController.getAffiliationsByActor(actor).size());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }
}