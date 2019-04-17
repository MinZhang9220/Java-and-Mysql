package java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
     * @param connection the sqlite3 database connection
     * @return true if the user was successfully inserted into the database. False otherwise.
     */
    public boolean insertActorIntoDatabase(Connection connection){
        try {
            if(isActorHomonym(connection)) {
                //to be updated
                if(!sendConfirmationMessage()){
                    return false;
                }
            }
            PreparedStatement statement = connection.prepareStatement("insert into actor(firstname, lastname, leveloftrust) values (?,?,?)");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setDouble(3, levelOfTrust);
            boolean result = statement.execute();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to check if the actor is a homonym actor, ie they have the same first name and last name.
     * Makes a query to the database to check if there are actors with the same first and last name.
     * If there are actors with the same first name and last name, returns true. Returns false otherwise.
     * @param connection the sqlite3 database connection
     * @return true if the actor is a homonym actor, false otherwise
     * @throws SQLException the SQL Exception if there is an error in the database execution
     */
    public boolean isActorHomonym(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select count(*) as total from actor where firstname = ? and lastname = ? values (?,?)");
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        ResultSet result = statement.executeQuery();
        int count = 0;
        while(result.next()){
            count = result.getInt("total");
        }
        if(count != 0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean sendConfirmationMessage(){
        return true;
    }
}

