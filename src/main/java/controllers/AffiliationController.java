package controllers;

import datahandling.AffiliationRepository;
import models.Actor;
import models.Affiliation;
import models.Organisation;
import views.AffiliationView;

import java.sql.Connection;
import java.util.Scanner;

public class AffiliationController {

    private AffiliationRepository affiliationRepository;
    private AffiliationView affiliationView;
    private OrganisationController organisationController;
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

    public void createAffiliation(Scanner scanner){
        Affiliation affiliation = new Affiliation();
        Organisation organisation = organisationController.getOrganisationFromUser(scanner);
        affiliation.setOrganisation(organisation);
        Actor actor = actorController.getActorFromUser(scanner);
        affiliation.setActor(actor);
        String role = affiliationView.getRole(scanner);
        while(!affiliation.setRole(role)){
            role = affiliationView.getRole(scanner);
        }
        String[] startEndDates = affiliationView.getStartEndDates(scanner);
        String potentialStartDate = startEndDates[0];
        String potentialEndDate = startEndDates[1];
        boolean validStartEndDate = false;
        while(!validStartEndDate){
            if(!affiliation.isValidDate(potentialStartDate)){
                //print invalid start date message
                affiliationView.printInvalidStartDateMessage();
            }
            else if(!affiliation.isValidDate(potentialEndDate)){
                //print invalid end date message
                affiliationView.printInvalidEndDateMessage();
            }
            else if(!affiliation.isValidStartEndDate(potentialStartDate,potentialEndDate)){
                //print start date is after end date message
                affiliationView.printInvalidStartEndDateMessage();
            }
            else{
                validStartEndDate = true;
            }
            if(validStartEndDate == false){
                startEndDates = affiliationView.getStartEndDates(scanner);
                potentialStartDate = startEndDates[0];
                potentialEndDate = startEndDates[1];
            }
        }
        affiliation.setStartDate(potentialStartDate);
        affiliation.setEndDate(potentialEndDate);
        if(!affiliation.insertAffiliationIntoDatabase(affiliationRepository)){
            affiliationView.printFailureMessage();
        }
        else{
            affiliationView.printSuccessMessage();
        }
    }
}
