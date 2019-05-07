package views;

import models.Actor;
import models.Affiliation;

import java.util.List;
import java.util.Scanner;

/**
 * View class for an actor. Used to provide feedback and receive input from a user
 * when they interact with functionality related to actors
 */
public class ActorView {

    /**
     * Gets the details of an actor, guiding the user through with print statements.
     * @param scanner the scanner used to receive user input
     * @return the string array with the results provided by the user
     */
    public String[] getActorDetails(Scanner scanner){
        System.out.println("Please type the actor's first name.");
        String firstName = scanner.nextLine();
        System.out.println("Please type the actor's last name.");
        String lastName = scanner.nextLine();
        System.out.println("Please type the actor's level of trust on a scale of 0.0 to 1.0 (leave blank for unknown).");
        String levelOfTrust = scanner.nextLine();
        String[] actorDetails = {firstName, lastName, levelOfTrust};
        return actorDetails;
    }

    /**
     * Gets the level of trust of an actor through the input of the user,
     * guiding the user through with print statements.
     * @param scanner the scanner used to receive the user input
     * @return the level of trust provided by the user
     */
    public String getLevelOfTrust(Scanner scanner){
        System.out.println("Invalid level of trust.");
        System.out.println("Please type the actor's level of trust on a scale of 0.0 to 1.0 (leave blank for unknown).");
        String levelOfTrust = scanner.nextLine();
        return levelOfTrust;
    }

    /**
     * Prints a message telling the user that the actor's first name was empty.
     */
    public void printFirstNameEmptyMessage(){
        System.out.println("The actor's first name is empty.");
    }

    /**
     * Prints a message telling the user that the actor's last name was empty.
     */
    public void printLastNameEmptyMessage(){
        System.out.println("The actor's last name is empty.");
    }

    /**
     * Prints a message telling the user that the actor they entered had homonym actor(s).
     * Iterates through the list of homonym actors and prints the details of all of them.
     */
    public String printHomonymConfirmMessage(List<Actor> homonymActors, Scanner scanner){
        System.out.println("Homonym actor(s) exists in the database with the following details:");
        for(Actor actor : homonymActors) {
            System.out.println("Actor id: " + actor.getActorid());
            System.out.println("Actor name: " + actor.getFirstName() + " " + actor.getLastName());
            System.out.println("Actor's level of trust: " + actor.getLevelOfTrust());
            List<Affiliation> affiliationList = actor.getAffiliations();
            if(affiliationList.size() > 0) {
                System.out.println("Affiliation(s): ");
                for (Affiliation affiliation : actor.getAffiliations()) {
                    System.out.println("Organisation: " + affiliation.getOrganisation().getOrganisationName());
                    if (affiliation.getStartDateString() != null) {
                        System.out.println("Start date: " + affiliation.getStartDateString());
                    }
                    if (affiliation.getEndDateString() != null) {
                        System.out.println("End date: " + affiliation.getEndDateString());
                    }
                    if (affiliation.getRole() != null) {
                        System.out.println("Role: " + affiliation.getRole());
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }
        System.out.println("Would you still like to add the actor into the system? (yes/no to answer)");
        String answer = scanner.nextLine();
        return answer;
    }

    /**
     * Prints a failure message when the actor is
     * unsuccessfully inserted into the database.
     */
    public void printFailureMessage(){
        System.out.println("The insert operation was unsuccessful.");
    }

    /**
     * Prints a success message that the actor was
     * successfully inserted into the database.
     */
    public void printSuccessMessage(){
        System.out.println("The actor was successfully inserted into the database.");
    }

    /**
     * Prints a list of actors from a list of actors with their actor id, first name and last name in a nice format
     * @param actorList the list of actors to be printed
     */
    public void printActors(List<Actor> actorList){
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("ACTORS");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for(Actor actor : actorList){
            System.out.println(actor.getActorid() + " " + actor.getFirstName() + " " + actor.getLastName());
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }

    /**
     * Gets an integer based on the user input which is later checked if it is an actor id.
     * Only accepts integers or prints a message that the input was not valid and prompts the user to try again
     * @param scanner the scanner to receive user input
     * @return an integer which should be the actor id
     */
    public Integer getActorIdFromUser(Scanner scanner){
        System.out.println("Enter an actor id: ");
        while (!scanner.hasNextInt()) {
            String input = scanner.next();
            System.out.printf("\"%s\" is not a valid number.\n", input);
        }
        Integer id = scanner.nextInt();
        scanner.nextLine();
        return id;
    }

    /**
     * Prints a message telling the user that the actor id they entered was an invalid actor that doesn't exist
     * in the database
     */
    public void printInvalidActorIdMessage(){
        System.out.println("Invalid actor id entered. Please try again.");
    }


}
