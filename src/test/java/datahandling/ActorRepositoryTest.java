package datahandling;

import controllers.ActorController;
import controllers.AffiliationController;
import controllers.OrganisationController;
import models.Actor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

class ActorRepositoryTest {

    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;
    TestDatabaseManager testDatabaseManager = new TestDatabaseManager();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    ActorController actorController;
    AffiliationController affiliationController;
    OrganisationController organisationController;
    ActorRepository actorRepository;

    @BeforeEach
    void setUp() {
        connection = sqLiteJDBC.getConnectionToDatabase("test");
        testDatabaseManager.populateDatabaseWithActors(connection);
        actorController = new ActorController(connection);
        organisationController = new OrganisationController(connection);
        actorRepository = new ActorRepository(connection);
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
    void insertActor() {
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
            assertTrue(count == 0);
            Actor actor = new Actor(5, "Midoriya", "Izuku", 1.0);
            actorRepository.insertActor(actor);
            statement = connection.prepareStatement(
                    "select count(*) as total from actor where firstname = ? and lastname = ? and leveloftrust = ?");
            statement.setString(1, "Midoriya");
            statement.setString(2, "Izuku");
            statement.setDouble(3, 1.0);
            result = statement.executeQuery();
            while (result.next()) {
                count = result.getInt("total");
            }
            assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void isActorHomonymWithHomonymActor() {
        Actor actor = new Actor(5,"Midoriya", "Izuku", 1.0);
        assertFalse(actorRepository.isActorHomonym(actor));
    }

    @Test
    void isActorHomonymWithNonHomonymActor() {
        Actor actor = new Actor(5,"Katsuki", "Bakugo", 0.0);
        assertTrue(actorRepository.isActorHomonym(actor));
    }

    @Test
    void getHomonymActors() {
        Actor actor = new Actor(5,"Katsuki", "Bakugo", 1.0);
        List<Actor> homonymActors = actorRepository.getHomonymActors(actor,affiliationController);
        assertEquals(1, homonymActors.size());
        Actor homonymActor = homonymActors.get(0);
        assertEquals(1, homonymActor.getActorid());
        assertEquals("Katsuki", homonymActor.getFirstName());
        assertEquals("Bakugo", homonymActor.getLastName());
        assertEquals(null, homonymActor.getLevelOfTrust());
        assertEquals(0, homonymActor.getAffiliations().size());
    }

    @Test
    void getActors() {
        List<Actor> actors = actorRepository.getActors();
        assertEquals(4, actors.size());
        assertEquals("Katsuki", actors.get(0).getFirstName());
        assertEquals("All", actors.get(1).getFirstName());
        assertEquals("Ochaco", actors.get(2).getFirstName());
        assertEquals("Tenya", actors.get(3).getFirstName());
    }

    @Test
    void getActorById() {
        Actor actor = actorRepository.getActorById(3);
        assertEquals("Ochaco", actor.getFirstName());
        assertEquals("Uraraka", actor.getLastName());
    }
}