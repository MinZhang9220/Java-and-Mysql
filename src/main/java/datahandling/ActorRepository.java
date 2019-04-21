package datahandling;

import models.Actor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorRepository {

    private Connection connection;

    public ActorRepository(Connection connection){
        this.connection = connection;
    }

    /**
     * Method to insert an organisation with a given organisation name in the database.
     * @param actor the actor to be inserted
     * @return true if the organisation is successfully inserted, false otherwise
     */
    public boolean insertActor(Actor actor){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into actor(firstname, lastname, leveloftrust) values (?,?,?)");
            preparedStatement.setString(1,actor.getFirstName());
            preparedStatement.setString(2,actor.getLastName());
            if(actor.getLevelOfTrust() != null) {
                preparedStatement.setDouble(3, actor.getLevelOfTrust());
            }
            else{
                preparedStatement.setNull(3, Types.REAL);
            }
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            if(count > 0){
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to query the database to see if the given actor has homonym actors.
     * The actor is a homonym actor if there are other actors
     * in the database with the same first and last name.
     * @param actor the actor to search for
     * @return true if the actor has homonym actors, false otherwise.
     */
    public boolean isActorHomonym(Actor actor){
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, actor.getFirstName());
            statement.setString(2, actor.getLastName());
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            if (count != 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to query the database for a list of actors with the same
     * first name and last name as the given actor, and then return the list.
     * @param actor the actor to find homonym actors from
     * @return the list of homonym actors
     */
    public List<Actor> getHomonymActors(Actor actor){
        try {
            PreparedStatement statement = connection.prepareStatement("select * from actor where firstname = ? and lastname = ?");
            statement.setString(1, actor.getFirstName());
            statement.setString(2, actor.getLastName());
            ResultSet result = statement.executeQuery();
            List<Actor> homonymActors = new ArrayList<>();
            while (result.next()) {
                Double levelOfTrust = (Double) result.getObject("levelOfTrust");
                Actor homonymActor = new Actor(result.getInt("actorid"),
                        result.getString("firstname"),
                        result.getString("lastname"),
                        levelOfTrust);
                homonymActors.add(homonymActor);
            }
            return homonymActors;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to query the database for a list of all actors, and then return the list.
     * @return the list of actors
     */
    public List<Actor> getActors(){
        try {
            PreparedStatement statement = connection.prepareStatement("select * from actor");
            ResultSet result = statement.executeQuery();
            List<Actor> actorsList = new ArrayList<>();
            while (result.next()) {
                Double levelOfTrust = (Double) result.getObject("levelOfTrust");
                Actor homonymActor = new Actor(result.getInt("actorid"),
                        result.getString("firstname"),
                        result.getString("lastname"),
                        levelOfTrust);
                actorsList.add(homonymActor);
            }
            return actorsList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Actor getActorById(Integer actorid){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from actor where actorid = ?");
            preparedStatement.setInt(1,actorid);
            preparedStatement.closeOnCompletion();
            ResultSet result = preparedStatement.executeQuery();
            Actor actor = null;
            while (result.next()) {
                Double levelOfTrust = (Double) result.getObject("levelOfTrust");
                actor = new Actor(result.getInt("actorid"),
                        result.getString("firstname"),
                        result.getString("lastname"),
                        levelOfTrust);
            }
            return actor;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
