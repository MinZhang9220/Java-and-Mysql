package models;

import controllers.ActorController;
import controllers.AffiliationController;
import controllers.OrganisationController;
import datahandling.SQLiteJDBC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steps.TestDatabaseManager;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class AffiliationTest {

    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;
    TestDatabaseManager testDatabaseManager = new TestDatabaseManager();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    ActorController actorController;
    AffiliationController affiliationController;
    OrganisationController organisationController;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @BeforeEach
    void setUp() {
        connection = sqLiteJDBC.getConnectionToDatabase("test");
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
    void insertAffiliationIntoDatabase() {

        Actor actor = new Actor(1, "Katsuki", "Bakugo", null);
        Organisation organisation = new Organisation("U.A");
        LocalDate startDate = LocalDate.parse("2018-01-05", formatter);
        LocalDate endDate = LocalDate.parse("2020-12-03", formatter);
        Affiliation affiliation = new Affiliation(actor, organisation, "Hero", startDate, endDate);
        assertTrue(affiliation.insertAffiliationIntoDatabase(affiliationController.getAffiliationRepository()));
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and role = ? and startdate = ? and enddate = ?");
            statement.setInt(1, 1);
            statement.setString(2, "U.A");
            statement.setString(3, "Hero");
            statement.setString(4, "2018-01-05");
            statement.setString(5, "2020-12-03");
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
    void isValidDateWithEmptyDate() {
        Affiliation affiliation = new Affiliation();
        String emptyDate = "";
        assertTrue(affiliation.isValidDate(emptyDate));
    }

    @Test
    void isValidDateWithValidDate() {
        Affiliation affiliation = new Affiliation();
        String validDate = "2019-04-20";
        assertTrue(affiliation.isValidDate(validDate));
    }

    @Test
    void isValidDateWithInvalidDate() {
        Affiliation affiliation = new Affiliation();
        String invalidDate = "ThisIsNotADate";
        assertFalse(affiliation.isValidDate(invalidDate));
    }

    /**
     * Checks if start date is prior to the end date
     */
    @Test
    void isValidStartEndDateWithStartDatePriorToEndDate() {
        String startDate = "2019-04-20";
        String endDate = "2019-04-21";
        Affiliation affiliation = new Affiliation();
        assertTrue(affiliation.isValidStartEndDate(startDate, endDate));
    }

    /**
     * Checks if start date is prior to the end date
     */
    @Test
    void isValidStartEndDateWithEndDatePriorToStartDate() {
        String startDate = "2019-04-21";
        String endDate = "2019-04-20";
        Affiliation affiliation = new Affiliation();
        assertFalse(affiliation.isValidStartEndDate(startDate, endDate));
    }

    /**
     * Empty start date is valid
     */
    @Test
    void isValidStartEndDateWithEmptyStartDate() {
        String startDate = "";
        String endDate = "2019-04-20";
        Affiliation affiliation = new Affiliation();
        assertTrue(affiliation.isValidStartEndDate(startDate, endDate));
    }

    /**
     * Empty end date is valid
     */
    @Test
    void isValidStartEndDateWithEmptyEndDate() {
        String startDate = "2019-04-20";
        String endDate = "";
        Affiliation affiliation = new Affiliation();
        assertTrue(affiliation.isValidStartEndDate(startDate, endDate));
    }


    /**
     * Purpose of the modified getter is it doesn't throw a null pointer exception
     */
    @Test
    void getRoleWithNullRole() {
        Affiliation affiliation = new Affiliation();
        assertNull(affiliation.getRole());
    }

    /**
     * Null role is valid
     */
    @Test
    void setRoleWithNullRole() {
        Affiliation affiliation = new Affiliation();
        String role = null;
        assertTrue(affiliation.setRole(role));
        assertNull(affiliation.getRole());
    }

    /**
     * Empty role is valid
     */
    @Test
    void setRoleWithEmptyRole() {
        Affiliation affiliation = new Affiliation();
        String role = "";
        assertTrue(affiliation.setRole(role));
        assertNull(affiliation.getRole());
    }

    /**
     * Role with less than 100 characters is valid
     */
    @Test
    void setRoleWithValidRole() {
        Affiliation affiliation = new Affiliation();
        String role = "Hero";
        assertTrue(affiliation.setRole(role));
        assertEquals("Hero", affiliation.getRole());
    }

    /**
     * Role with less than 100 characters is valid
     */
    @Test
    void setRoleWithValidRoleMoreThan100CharactersLong() {
        Affiliation affiliation = new Affiliation();
        String role = "ThisHasMoreThanOneHundredCharactersAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        assertFalse(affiliation.setRole(role));
        assertNull(affiliation.getRole());
    }

    @Test
    void getStartDateStringWithNullStartDate() {
        Affiliation affiliation = new Affiliation();
        assertNull(affiliation.getStartDateString());
    }

    @Test
    void getStartDateStringWithValidStartDate() {
        Actor actor = new Actor(1, "Katsuki", "Bakugo", null);
        Organisation organisation = new Organisation("U.A");
        LocalDate startDate = LocalDate.parse("2018-01-05", formatter);
        LocalDate endDate = LocalDate.parse("2020-12-03", formatter);
        Affiliation affiliation = new Affiliation(actor, organisation, "Hero", startDate, endDate);
        assertEquals("2018-01-05", affiliation.getStartDateString());
    }

    @Test
    void setStartDateWithEmptyString() {
        Affiliation affiliation = new Affiliation();
        String emptyString = "";
        affiliation.setStartDate(emptyString);
        assertNull(affiliation.getStartDateString());
    }

    @Test
    void setStartDateWithValidDateString() {
        Affiliation affiliation = new Affiliation();
        String validDateString = "2018-08-23";
        affiliation.setStartDate(validDateString);
        assertEquals("2018-08-23", affiliation.getStartDateString());
    }


    @Test
    void getEndDateStringWithNullStartDate() {
        Affiliation affiliation = new Affiliation();
        assertNull(affiliation.getEndDateString());
    }

    @Test
    void getEndDateStringWithValidStartDate() {
        Actor actor = new Actor(1, "Katsuki", "Bakugo", null);
        Organisation organisation = new Organisation("U.A");
        LocalDate startDate = LocalDate.parse("2018-01-05", formatter);
        LocalDate endDate = LocalDate.parse("2020-12-03", formatter);
        Affiliation affiliation = new Affiliation(actor, organisation, "Hero", startDate, endDate);
        assertEquals("2020-12-03", affiliation.getEndDateString());
    }

    @Test
    void setEndDateWithEmptyString() {
        Affiliation affiliation = new Affiliation();
        String emptyString = "";
        affiliation.setEndDate(emptyString);
        assertNull(affiliation.getEndDateString());
    }

    @Test
    void setEndDateWithValidDateString() {
        Affiliation affiliation = new Affiliation();
        String validDateString = "2018-08-23";
        affiliation.setEndDate(validDateString);
        assertEquals("2018-08-23", affiliation.getEndDateString());
    }
}