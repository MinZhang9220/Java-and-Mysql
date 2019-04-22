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
                affiliation.setStartDate(potentialStartDate);
                affiliation.setEndDate(potentialEndDate);
                if (!affiliation.insertAffiliationIntoDatabase(affiliationRepository)) {
                    affiliationView.printFailureMessage();
                } else {
                    affiliationView.printSuccessMessage();
                }
            }
        }
    }

    public List<Affiliation> getAffiliationsByActor(Actor actor){
        return affiliationRepository.getAffiliationsByActor(actor, organisationController.getOrganisationRepository());
    }
}
