package models;

import controllers.ActorController;
import controllers.AffiliationController;
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
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ActorTest {

    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;
    TestDatabaseManager testDatabaseManager = new TestDatabaseManager();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    ActorController actorController;
    AffiliationController affiliationController;
    OrganisationController organisationController;

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
    void insertActorIntoDatabase() {
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
            assertEquals(0, count);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
        Actor actor = new Actor(5, "Midoriya", "Izuku", 1.0);
        assertTrue(actor.insertActorIntoDatabase(actorController.getActorRepository()));
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
            assertEquals(1, count);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void isActorHomonymWithHomonymActor() {
        testDatabaseManager.populateDatabaseWithActors(connection);
        Actor actor = new Actor(5, "Katsuki", "Bakugo", 0.0);
        assertTrue(actor.isActorHomonym(actorController.getActorRepository()));
    }

    @Test
    void isActorHomonymWithNoHomonymActor() {
        Actor actor = new Actor(5, "Katsuki", "Bakugo", 0.0);
        assertFalse(actor.isActorHomonym(actorController.getActorRepository()));
    }

    @Test
    void getHomonymActorsIncludingAffiliations() {
        testDatabaseManager.populateDatabaseWithActors(connection);
        testDatabaseManager.populateDatabaseWithOrganisations(connection);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Actor actor = new Actor(5, "Katsuki", "Bakugo", 0.0);
        List<Actor> actorList = actor.getHomonymActors(actorController.getActorRepository(), affiliationController);
        assertEquals(1, actorList.size());
        Actor homonymActor = actorList.get(0);
        Affiliation affiliation = homonymActor.getAffiliations().get(0);
        assertEquals("Katsuki", homonymActor.getFirstName());
        assertEquals("Bakugo", homonymActor.getLastName());
        assertNull(homonymActor.getLevelOfTrust());
        assertEquals("U.A", affiliation.getOrganisation().getOrganisationName());
        assertEquals("Hero", affiliation.getRole());
        assertEquals("2018-01-05", affiliation.getStartDateString());
        assertEquals("2020-12-03", affiliation.getEndDateString());
        assertEquals(1, affiliation.getActor().getActorid());
    }

    /**
     * Empty level of trust should not affect the current value of level of trust.
     */
    @Test
    void setLevelOfTrustWithEmptyLevelOfTrust() {
        String levelOfTrust = "";
        Actor actor = new Actor(5, "Midoriya", "Izuku", 1.0);
        assertTrue(actor.setLevelOfTrust(levelOfTrust));
        assertEquals(1.0, actor.getLevelOfTrust());
    }

    @Test
    void setLevelOfTrustWithValidLevelOfTrust() {
        String levelOfTrust = "0.42";
        Actor actor = new Actor(5, "Midoriya", "Izuku", 1.0);
        assertTrue(actor.setLevelOfTrust(levelOfTrust));
        assertEquals(0.42, actor.getLevelOfTrust());
    }

    @Test
    void setLevelOfTrustWithInvalidLevelOfTrust() {
        String levelOfTrust = "ThisIsInvalid";
        Actor actor = new Actor(5, "Midoriya", "Izuku", 1.0);
        assertFalse(actor.setLevelOfTrust(levelOfTrust));
        assertEquals(1.0, actor.getLevelOfTrust());
    }

    @Test
    void setLevelOfTrustWithLevelOfTrustGreaterThanOne() {
        String levelOfTrust = "1.1";
        Actor actor = new Actor(5, "Midoriya", "Izuku", 1.0);
        assertFalse(actor.setLevelOfTrust(levelOfTrust));
        assertEquals(1.0, actor.getLevelOfTrust());
    }

    @Test
    void setLevelOfTrustWithLevelOfTrustLessThanZero() {
        String levelOfTrust = "-0.1";
        Actor actor = new Actor(5, "Midoriya", "Izuku", 1.0);
        assertFalse(actor.setLevelOfTrust(levelOfTrust));
        assertEquals(1.0, actor.getLevelOfTrust());
    }

    @Test
    void setLevelOfTrustWithLevelOfTrustEqualToZero() {
        String levelOfTrust = "0.0";
        Actor actor = new Actor(5, "Midoriya", "Izuku", 1.0);
        assertTrue(actor.setLevelOfTrust(levelOfTrust));
        assertEquals(0.0, actor.getLevelOfTrust());
    }

    @Test
    void setLevelOfTrustWithLevelOfTrustEqualToOne() {
        String levelOfTrust = "1.0";
        Actor actor = new Actor(5, "Midoriya", "Izuku", 0.0);
        assertTrue(actor.setLevelOfTrust(levelOfTrust));
        assertEquals(1.0, actor.getLevelOfTrust());
    }

    @Test
    void actorConstructor(){
        Actor actor = new Actor("Midoriya", "Izuku");
        assertEquals("Midoriya", actor.getFirstName());
        assertEquals("Izuku", actor.getLastName());
    }

}