package controllers;

import datahandling.SQLiteJDBC;
import models.Organisation;
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

class OrganisationControllerTest {

    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;
    TestDatabaseManager testDatabaseManager = new TestDatabaseManager();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    OrganisationController organisationController;
    Scanner scanner;

    @BeforeEach
    public void setUp() {
        connection = sqLiteJDBC.getConnectionToDatabase("test");
        testDatabaseManager.populateDatabaseWithOrganisations(connection);
        organisationController = new OrganisationController(connection);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        testDatabaseManager.resetDatabase(connection);
    }

    @Test
    void createOrganisationWithNonExistingOrganisationName() {
        final InputStream original = System.in;
        String input = "uniqueOrganisation";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        organisationController.createOrganisation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from organisation where name = ?");
            statement.setString(1, "uniqueOrganisation");
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(1, count);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void createOrganisationWithExistingOrganisationName() {
        final InputStream original = System.in;
        String input = "nonUniqueOrganisation";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        organisationController.createOrganisation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from organisation where name = ?");
            statement.setString(1, "nonUniqueOrganisation");
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(1, count);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void createOrganisationWithAnEmptyName() {
        final InputStream original = System.in;
        String input = "\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        organisationController.createOrganisation(scanner);
        System.setIn(original);
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from organisation where name = ?");
            statement.setString(1, "");
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(0, count);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getOrganisationFromUserWithValidOrganisation() {
        final InputStream original = System.in;
        String input = "nonUniqueOrganisation";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Organisation organisation = organisationController.getOrganisationFromUser(scanner);
        System.setIn(original);
        assertNotNull(organisation);
        assertEquals("nonUniqueOrganisation", organisation.getOrganisationName());
    }

    @Test
    void getOrganisationFromUserWithInvalidOrganisation() {
        final InputStream original = System.in;
        String input = "nonExistingOrganisation";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Organisation organisation = null;
        try {
            organisation = organisationController.getOrganisationFromUser(scanner);
        }
        catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        assertNull(organisation);
    }

    @Test
    void getOrganisationFromUserWithNoOrganisations() {
        try {
            PreparedStatement statement = connection.prepareStatement("delete from organisation");
            statement.closeOnCompletion();
            assertEquals(4,statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scanner = new Scanner(System.in);
        Organisation organisation = organisationController.getOrganisationFromUser(scanner);
        assertNull(organisation);
    }

    @Test
    void getOrganisationFromUserWithInvalidInputThenValidInput() {
        final InputStream original = System.in;
        String input = "nonExistingOrganisation\nnonUniqueOrganisation";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Organisation organisation = null;
        organisation = organisationController.getOrganisationFromUser(scanner);
        System.setIn(original);
        assertNotNull(organisation);
        assertEquals("nonUniqueOrganisation", organisation.getOrganisationName());
    }

    @Test
    void getOrganisationRepository() {
        assertNotNull(organisationController.getOrganisationRepository());
    }

}