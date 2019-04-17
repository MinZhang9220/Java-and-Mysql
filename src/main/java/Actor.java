import java.sql.Connection;
import java.sql.PreparedStatement;

public class Actor {

    private int actorid;
    private String firstName;
    private String lastName;
    private double levelOfTrust;

    /**
     * Constructs an Actor with a given actor id, first name, last name and level of trust.
     * @param actorid the actor's id
     * @param firstName the actor's first name
     * @param lastName the actor's last name
     * @param levelOfTrust the actor's level of trust
     */
    public Actor(int actorid, String firstName, String lastName, double levelOfTrust){
        this.actorid = actorid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.levelOfTrust = levelOfTrust;
    }

    /**
     * Inserts the actor and its attributes into the database where it will be stored persistently.
     */
    public void insertActorIntoDatabase(Connection connection, Actor actor){
        //PreparedStatement statement = connection.
    }
}
