package controllers;

import datahandling.AffiliationRepository;
import datahandling.OrganisationRepository;
import models.Actor;
import models.Affiliation;
import models.Organisation;
import views.AffiliationView;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

/**
 * The affiliation controller uses the affiliation model and view to perform affiliation functionality,
 * which is currently adding affiliations into the system and retrieving affiliations
 */
public class AffiliationController {

    /**
     * The affiliation repository used to make queries to the database
     */
    private AffiliationRepository affiliationRepository;
    /**
     * The  affiliation view used to provide feedback to the user on their actions
     */
    private AffiliationView affiliationView;
    /**
     * The organisation controller used to link an affiliation to an organisation
     */
    private OrganisationController organisationController;
    /**
     * The actor controller used to link an actor to an organisation
     */
    private ActorController actorController;

    /**
     * Constructor for the affiliation controller class.
     * Initialises the actor repository for talking to the sqlite database
     * and initialises actor view to update what the user sees.
     * @param connection the sqlite connection
     */
    public AffiliationController(Connection connection, ActorController actorController, OrganisationController organisationController){
        affiliationRepository = new AffiliationRepository(connection);
        affiliationView = new AffiliationView();
        this.organisationController = organisationController;
        this.actorController = actorController;
    }


    /**
     * Method to create an affiliation. Requires there to be at least one organisation and actor in the database.
     * Validates the affiliation according to the ACs for story 2.
     * If successful, creates a valid affiliation based on user input and stores it into the database
     * @param scanner the scanner used to receive user input
     */
    public void createAffiliation(Scanner scanner){
        Affiliation affiliation = new Affiliation();
        Organisation organisation = organisationController.getOrganisationFromUser(scanner);
        if(organisation == null){
            affiliationView.printNoOrganisationsMessage();
        }
        else {
            affiliation.setOrganisation(organisation);
            Actor actor = actorController.getActorFromUser(scanner);
            if(actor == null){
                affiliationView.printNoActorsMessage();
            }
            else {
                affiliation.setActor(actor);
                String role = affiliationView.getRole(scanner);
                while (!affiliation.setRole(role)) {
                    role = affiliationView.getRole(scanner);
                }
                String[] startEndDates = getValidStartEndDateFromUser(scanner, affiliation);
                affiliation.setStartDate(startEndDates[0]);
                affiliation.setEndDate(startEndDates[1]);
                if (!affiliation.insertAffiliationIntoDatabase(affiliationRepository)) {
                    affiliationView.printFailureMessage();
                } else {
                    affiliationView.printSuccessMessage();
                }
            }
        }
    }

    /**
     * Method to get a valid start and end date in a string format from a user.
     * A valid start and end date (as shown from the ACs) is a string in the format of
     * yyyy-MM-dd or an empty string.
     * @param scanner the scanner to receive user input
     * @param affiliation the affiliation to be added
     * @return A string array of size two where the first index is the start date and the second index is the end date
     */
    public String[] getValidStartEndDateFromUser(Scanner scanner, Affiliation affiliation){
        String[] startEndDates = affiliationView.getStartEndDates(scanner);
        String potentialStartDate = startEndDates[0];
        String potentialEndDate = startEndDates[1];
        boolean validStartEndDate = false;
        while (!validStartEndDate) {
            if (!affiliation.isValidDate(potentialStartDate)) {
                affiliationView.printInvalidStartDateMessage();
            } else if (!affiliation.isValidDate(potentialEndDate)) {
                affiliationView.printInvalidEndDateMessage();
            } else if (!affiliation.isValidStartEndDate(potentialStartDate, potentialEndDate)) {
                affiliationView.printInvalidStartEndDateMessage();
            } else {
                validStartEndDate = true;
            }
            if (validStartEndDate == false) {
                startEndDates = affiliationView.getStartEndDates(scanner);
                potentialStartDate = startEndDates[0];
                potentialEndDate = startEndDates[1];
            }
        }
        return startEndDates;
    }

    public List<Affiliation> getAffiliationsByActor(Actor actor){
        return affiliationRepository.getAffiliationsByActor(actor, organisationController.getOrganisationRepository());
    }

    public AffiliationRepository getAffiliationRepository(){
        return affiliationRepository;
    }
}
