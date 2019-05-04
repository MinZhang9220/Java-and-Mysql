package datahandling;

import controllers.OrganisationController;
import models.Organisation;
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
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class OrganisationRepositoryTest {

    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;
    TestDatabaseManager testDatabaseManager = new TestDatabaseManager();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    OrganisationController organisationController;
    OrganisationRepository organisationRepository;

    @BeforeEach
    public void setUp() {
        connection = sqLiteJDBC.getConnectionToDatabase("test");
        testDatabaseManager.populateDatabaseWithOrganisations(connection);
        organisationController = new OrganisationController(connection);
        organisationRepository = organisationController.getOrganisationRepository();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        testDatabaseManager.resetDatabase(connection);
    }

    @Test
    void insertOrganisation() {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from organisation where name = ?");
            statement.setString(1, "MeTooThanks");
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(0, count);
            organisationRepository.insertOrganisation("MeTooThanks");
            statement = connection.prepareStatement("select count(*) as total from organisation where name = ?");
            statement.setString(1, "MeTooThanks");
            result = statement.executeQuery();
            count = 0;
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
    void isDuplicateOrganisationWithNoDuplicates() {
        assertFalse(organisationRepository.isDuplicateOrganisation("MeTooThanks"));
    }

    @Test
    void isDuplicateOrganisationWithDuplicates() {
        organisationRepository.insertOrganisation("MeTooThanks");
        assertTrue(organisationRepository.isDuplicateOrganisation("MeTooThanks"));

    }

    @Test
    void getOrganisations() {
        List<Organisation> organisationList = organisationRepository.getOrganisations();
        assertEquals(4, organisationList.size());
        assertEquals("test", organisationList.get(0).getOrganisationName());
        assertEquals("University of Canterbury", organisationList.get(1).getOrganisationName());
        assertEquals("U.A", organisationList.get(2).getOrganisationName());
        assertEquals("nonUniqueOrganisation", organisationList.get(3).getOrganisationName());

    }

    @Test
    void getOrganisationByName() {
        Organisation organisation = organisationRepository.getOrganisationByName("U.A");
        assertNotNull(organisation);
        assertEquals("U.A", organisation.getOrganisationName());
    }
}