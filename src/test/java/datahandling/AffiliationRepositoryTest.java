package datahandling;

import controllers.ActorController;
import controllers.AffiliationController;
import controllers.OrganisationController;
import models.Actor;
import models.Affiliation;
import models.Organisation;
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

class AffiliationRepositoryTest {

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
    AffiliationRepository affiliationRepository;
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
        affiliationRepository = affiliationController.getAffiliationRepository();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        testDatabaseManager.resetDatabase(connection);
    }

    @Test
    void insertAffiliation() {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and role = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "Teacher");
            statement.setString(4, "2018-12-05");
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            assertEquals(0, count);
            Actor actor = new Actor(2, "All", "Might", 1.0);
            Organisation organisation = new Organisation("U.A");
            Affiliation affiliation = new Affiliation();
            affiliation.setActor(actor);
            affiliation.setOrganisation(organisation);
            affiliation.setStartDate("");
            affiliation.setEndDate("2018-12-05");
            affiliation.setRole("Teacher");
            affiliationRepository.insertAffiliation(affiliation);
            statement = connection.prepareStatement(
                    "select count(*) as total from affiliation where actorid = ? and organisationname = ?" +
                            " and role = ? and enddate = ?");
            statement.setInt(1, 2);
            statement.setString(2, "U.A");
            statement.setString(3, "Teacher");
            statement.setString(4, "2018-12-05");
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
    void getAffiliationsByActor() {
        try{
            Actor actor = new Actor(2, "All", "Might", 1.0);
            assertEquals(0, affiliationRepository.getAffiliationsByActor(actor,
                    organisationController.getOrganisationRepository()).size());
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into affiliation(actorid,organisationname,role,startdate,enddate) values (?,?,?,?,?)");
            preparedStatement.setInt(1,2);
            preparedStatement.setString(2,"U.A");
            preparedStatement.setString(3,"Teacher");
            preparedStatement.setString(4,"2017-11-06");
            preparedStatement.setString(5,"2018-12-05");
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            assertTrue(count == 1);

            assertEquals(1, affiliationRepository.getAffiliationsByActor(actor,
                    organisationController.getOrganisationRepository()).size());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }
}