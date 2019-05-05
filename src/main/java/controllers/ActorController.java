package controllers;

import datahandling.ActorRepository;
import models.Actor;
import views.ActorView;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

/**
 * The actor controller uses the actor model and view to perform actor functionality,
 * which is currently adding actors into the system and retrieving actors
 */
public class ActorController {

    /**
     * The actor repository used to make queries to the database
     */
    private ActorRepository actorRepository;

    /**
     * The  actor view used to receive input and provide feedback to the user on their actions
     */
    private ActorView actorView;

    /**
     * The affiliation controller used to link an affiliation to an actor
     */
    private AffiliationController affiliationController;

    /**
     * Constructor for the actor controller class.
     * Initialises the actor repository for talking to the sqlite database
     * and initialises actor view to update what the user sees.
     * @param connection the sqlite connection
     */
    public ActorController(Connection connection){
        actorRepository = new ActorRepository(connection);
        actorView = new ActorView();
    }


    /**
     * Method to create an actor following the ACs specified.
     * If successful, creates a valid actor based on user input and stores it into the database
     * @param scanner the scanner to receive user input
     */
    public void createActor(Scanner scanner){
        String[] actorDetails = actorView.getActorDetails(scanner);
        String firstName = actorDetails[0];
        String lastName = actorDetails[1];
        String levelOfTrust = actorDetails[2];
        Actor actor = new Actor(firstName, lastName);
        while(!actor.setLevelOfTrust(levelOfTrust)){
            levelOfTrust = actorView.getLevelOfTrust(scanner);
        }
        if(actor.getFirstName().isEmpty()){
            actorView.printFirstNameEmptyMessage();
        }
        else if(actor.getLastName().isEmpty()){
            actorView.printLastNameEmptyMessage();
        }
        else if(actor.isActorHomonym(actorRepository)){
            if(!getHomonymActorConfirmationFromUser(scanner, actor)){
                actorView.printFailureMessage();
            }
            else if(!actor.insertActorIntoDatabase(actorRepository)){
                actorView.printFailureMessage();
            }
            else{
                actorView.printSuccessMessage();
            }
        }
        else if(!actor.insertActorIntoDatabase(actorRepository)){
            actorView.printFailureMessage();
        }
        else{
            actorView.printSuccessMessage();
        }
    }

    /**
     * Method to check if the user still wants to add an actor into the database
     * if there is a homonym actor through their input from a scanner.
     * Returns true if the user still wants to add the actor, false otherwise.
     * @param scanner the scanner to receive user input
     * @param actor the actor that has homonym actors
     * @return true if the actor should be actor, false if the actor shouldn't
     */
    public boolean getHomonymActorConfirmationFromUser(Scanner scanner, Actor actor){
        List<Actor> homonymActors = actor.getHomonymActors(actorRepository,affiliationController);
        String confirmationAnswer = "undefined";
        while(!confirmationAnswer.equalsIgnoreCase("yes")
                && !confirmationAnswer.equalsIgnoreCase("no")){
            confirmationAnswer = actorView.printHomonymConfirmMessage(homonymActors, scanner);
        }
        if(confirmationAnswer.equalsIgnoreCase("no")){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Method to get an actor based on the user's input on what actor they want.
     * @param scanner the scanner to get the user's input
     * @return the actor based on the user's input
     */
    public Actor getActorFromUser(Scanner scanner){
        List<Actor> actorList = actorRepository.getActors();
        if(actorList.size() == 0){
            return null;
        }
        else {
            actorView.printActors(actorList);
            Integer id = actorView.getActorIdFromUser(scanner);
            Actor actor = actorRepository.getActorById(id);
            while (actor == null) {
                actorView.printInvalidActorIdMessage();
                id = actorView.getActorIdFromUser(scanner);
                actor = actorRepository.getActorById(id);
            }
            return actor;
        }
    }

    public AffiliationController getAffiliationController() {
        return affiliationController;
    }

    public void setAffiliationController(AffiliationController affiliationController) {
        this.affiliationController = affiliationController;
    }

    public ActorRepository getActorRepository() {
        return actorRepository;
    }
}
