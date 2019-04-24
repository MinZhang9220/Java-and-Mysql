package controllers;

import datahandling.SQLiteJDBC;
import models.Actor;
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

class ActorControllerTest {

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
    void createActorWithUniqueName() {
        final InputStream original = System.in;
        String input = "Midoriya\nIzuku\n1.0";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        actorController.createActor(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from actor where firstname = ? and lastname = ? and leveloftrust = ?");
            statement.setString(1, "Midoriya");
            statement.setString(2, "Izuku");
            statement.setDouble(3, 1.0);
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
    void createActorWithEmptyFirstName() {
        final InputStream original = System.in;
        String input = "\nIzuku\n1.0";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        actorController.createActor(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from actor where firstname = ? and lastname = ? and leveloftrust = ?");
            statement.setString(1, "");
            statement.setString(2, "Izuku");
            statement.setDouble(3, 1.0);
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
    void createActorWithEmptyLastName() {
        final InputStream original = System.in;
        String input = "Midoriya\n\n1.0";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        actorController.createActor(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from actor where firstname = ? and lastname = ? and leveloftrust = ?");
            statement.setString(1, "Midoriya");
            statement.setString(2, "");
            statement.setDouble(3, 1.0);
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
    void createActorWithInvalidLevelOfTrust() {
        final InputStream original = System.in;
        String input = "Midoriya\nIzuku\ninvalidLevelOfTrust";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try{
        actorController.createActor(scanner);
        }
        catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from actor where firstname = ? and lastname = ? and leveloftrust = ?");
            statement.setString(1, "Midoriya");
            statement.setString(2, "Izuku");
            statement.setString(3, "invalidLevelOfTrust");
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
    void createActorWithUniqueNameAndNullLevelOfTrust() {
        final InputStream original = System.in;
        String input = "Midoriya\nIzuku\n\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        actorController.createActor(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from actor where firstname = ? and lastname = ?");
            statement.setString(1, "Midoriya");
            statement.setString(2, "Izuku");
            ResultSet result = statement.executeQuery();
            Double levelOfTrust = 0.0;
            while (result.next()) {
                levelOfTrust = (Double) result.getObject("leveloftrust");
            }
            assertNull(levelOfTrust);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createActorWithNonUniqueNameWithoutConfirming() {
        final InputStream original = System.in;
        String input = "Katsuki\nBakugo\n0.0";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try{
        actorController.createActor(scanner);
        }
        catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, "Katsuki");
            statement.setString(2, "Bakugo");
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
    void createActorWithNonUniqueNameWithDenyingConfirmation() {
        final InputStream original = System.in;
        String input = "Katsuki\nBakugo\n0.0\nno";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
            actorController.createActor(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, "Katsuki");
            statement.setString(2, "Bakugo");
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
    void createActorWithNonUniqueNameWithAcceptingConfirmation() {
        final InputStream original = System.in;
        String input = "Katsuki\nBakugo\n0.0\nyes";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        actorController.createActor(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, "Katsuki");
            statement.setString(2, "Bakugo");
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createActorWithNonUniqueNameWithNonYesOrNoAnswerFollowedByYes() {
        final InputStream original = System.in;
        String input = "Katsuki\nBakugo\n0.0\nasdasdsad\nyes";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        actorController.createActor(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, "Katsuki");
            statement.setString(2, "Bakugo");
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    void getHomonymActorConfirmationFromUserWithConfirmationAccepted() {
        Actor actor = new Actor("All", "Might");
        final InputStream original = System.in;
        String input = "yes";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        assertTrue(actorController.getHomonymActorConfirmationFromUser(scanner,actor));
        System.setIn(original);
    }

    @Test
    void getHomonymActorConfirmationFromUserWithConfirmationDeclined() {
        Actor actor = new Actor("All", "Might");
        final InputStream original = System.in;
        String input = "no";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        assertFalse(actorController.getHomonymActorConfirmationFromUser(scanner,actor));
        System.setIn(original);
    }

    @Test
    void getHomonymActorConfirmationFromUserWithConfirmationInvalidAndThenDeclined() {
        Actor actor = new Actor("All", "Might");
        final InputStream original = System.in;
        String input = "thisisinvalid\nno";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        assertFalse(actorController.getHomonymActorConfirmationFromUser(scanner,actor));
        System.setIn(original);
    }

    @Test
    void getHomonymActorConfirmationFromUserWithConfirmationInvalidAndThenAccepted() {
        Actor actor = new Actor("All", "Might");
        final InputStream original = System.in;
        String input = "thisisinvalid\nyes";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        assertTrue(actorController.getHomonymActorConfirmationFromUser(scanner,actor));
        System.setIn(original);
    }

    @Test
    void getActorFromUserWithValidActorInput() {
        final InputStream original = System.in;
        String input = "1\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Actor actor = actorController.getActorFromUser(scanner);
        System.setIn(original);
        assertEquals(1, actor.getActorid());
        assertEquals("Katsuki", actor.getFirstName());
        assertEquals("Bakugo", actor.getLastName());
        assertNull(actor.getLevelOfTrust());
    }

    @Test
    void getActorFromUserWithInvalidActorIdInput() {
        final InputStream original = System.in;
        String input = "10\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Actor actor = null;
        try{
        actor = actorController.getActorFromUser(scanner);
        }
        catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        assertNull(actor);
    }

    @Test
    void getActorFromUserWithInvalidActorInput() {
        final InputStream original = System.in;
        String input = "asdasd\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Actor actor = null;
        try{
            actor = actorController.getActorFromUser(scanner);
        }
        catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        assertNull(actor);
    }

    @Test
    void getActorFromUserWithInvalidActorInputThenValidActorInput() {
        final InputStream original = System.in;
        String input = "10\n2\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Actor actor = actorController.getActorFromUser(scanner);
        System.setIn(original);
        assertEquals(2, actor.getActorid());
        assertEquals("All", actor.getFirstName());
        assertEquals("Might", actor.getLastName());
        assertNull(actor.getLevelOfTrust());
    }

    @Test
    void getActorFromUserWithNoActorsInDatabase() {
        try {
            PreparedStatement statement = connection.prepareStatement("delete from actor");
            statement.closeOnCompletion();
            assertEquals(4,statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final InputStream original = System.in;
        String input = "1\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Actor actor = actorController.getActorFromUser(scanner);
        System.setIn(original);
        assertNull(actor);
    }

    @Test
    void setAndGetAffiliationController() {
        actorController = new ActorController(connection);
        organisationController = new OrganisationController(connection);
        affiliationController = new AffiliationController(connection, actorController, organisationController);
        assertNull(actorController.getAffiliationController());
        actorController.setAffiliationController(affiliationController);
        assertEquals(affiliationController, actorController.getAffiliationController());
    }
}