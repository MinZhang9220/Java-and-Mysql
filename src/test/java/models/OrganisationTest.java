package models;

import controllers.OrganisationController;
import datahandling.SQLiteJDBC;
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

class OrganisationTest {

    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;
    TestDatabaseManager testDatabaseManager = new TestDatabaseManager();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    OrganisationController organisationController;

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
    void isValidOrganisationNameWithValidOrganisationName() {
        Organisation organisation = new Organisation("uniqueName");
        assertTrue(organisation.isValidOrganisationName(organisationController.getOrganisationRepository()));
    }

    @Test
    void isValidOrganisationNameWithEmptyOrganisationName() {
        Organisation organisation = new Organisation("");
        assertFalse(organisation.isValidOrganisationName(organisationController.getOrganisationRepository()));
    }

    @Test
    void isValidOrganisationNameWithDuplicateOrganisationName() {
        Organisation organisation = new Organisation("nonUniqueOrganisation");
        assertFalse(organisation.isValidOrganisationName(organisationController.getOrganisationRepository()));
    }

    @Test
    void insertOrganisation() {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from organisation where name = ?");
            statement.setString(1, "insertOrganisationTest");
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
        Organisation organisation = new Organisation("insertOrganisationTest");
        assertTrue(organisation.insertOrganisation(organisationController.getOrganisationRepository()));
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from organisation where name = ?");
            statement.setString(1, "insertOrganisationTest");
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
}