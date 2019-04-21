package models;

import datahandling.ActorRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Actor {

    private int actorid;
    private String firstName;
    private String lastName;
    private Double levelOfTrust;

    /**
     * Constructs an Actor with a given actor id, first name, last name and level of trust.
     * @param actorid the actor's id
     * @param firstName the actor's first name
     * @param lastName the actor's last name
     * @param levelOfTrust the actor's level of trust
     */
    public Actor(int actorid, String firstName, String lastName, Double levelOfTrust){
        this.actorid = actorid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.levelOfTrust = levelOfTrust;
    }

    /**
     * Constructs an Actor with a given actor id, first name and last name.
     * @param firstName the actor's first name
     * @param lastName the actor's last name
     */
    public Actor(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Inserts the actor and its attributes into the database where it will be stored persistently.
     * @param actorRepository the repository used to make queries to the database
     * @return true if the user was successfully inserted into the database. False otherwise.
     */
    public boolean insertActorIntoDatabase(ActorRepository actorRepository){
        return actorRepository.insertActor(this);
    }

    /**
     * Method to check if the actor is a homonym actor, ie they have the same first name and last name.
     * Makes a query to the database to check if there are actors with the same first and last name.
     * If there are actors with the same first name and last name, returns true. Returns false otherwise.
     * @param actorRepository the repository used to make queries to the database
     * @return true if the actor is a homonym actor, false otherwise
     */
    public boolean isActorHomonym(ActorRepository actorRepository){
        return actorRepository.isActorHomonym(this);
    }

    /**
     * Method to get a list of homonym actors
     * @param actorRepository the repository used to make queries to the database
     * @return a list of homonym actors
     */
    public List<Actor> getHomonymActors(ActorRepository actorRepository){
        return actorRepository.getHomonymActors(this);
    }

    /**
     * Gets the actor's id
     * @return the actor's id
     */
    public int getActorid() {
        return actorid;
    }

    /**
     * Gets the actor's first name
     * @return the actor's first name
     */
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Double getLevelOfTrust() {
        return levelOfTrust;
    }

    /**
     * Method to set the actor's level of trust if it passes the validation checks.
     * The level of trust is set to null if the field is empty.
     * The level of trust is set if the value is between 0.0 and 1.0
     * The function returns true if the level of trust is set, false otherwise.
     * @param levelOfTrust the input string
     * @return true if level of trust is set, false otherwise
     */
    public boolean setLevelOfTrust(String levelOfTrust) {
        try{
            if(!levelOfTrust.isEmpty()){
                Double potentialLevelOfTrust = Double.parseDouble(levelOfTrust);
                if(potentialLevelOfTrust < 0.0 || potentialLevelOfTrust > 1.0){
                    return false;
                }
                else{
                    this.levelOfTrust = potentialLevelOfTrust;
                    return true;
                }
            }
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }
}

