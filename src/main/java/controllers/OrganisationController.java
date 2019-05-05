package controllers;

import datahandling.OrganisationRepository;
import models.Organisation;
import views.OrganisationView;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;


/**
 * The organisation controller uses the organisation model and view to perform organisation functionality,
 * which is currently adding organisations into the system and retrieving organisations
 */
public class OrganisationController {

    /**
     * The organisation repository used to make queries to the database
     */
    private OrganisationRepository organisationRepository;

    /**
     * The organisation view used to receive input and provide feedback to the user on their actions
     */
    private OrganisationView organisationView;

    /**
     * Constructor for the organisation controller class.
     * Initialises the organisation repository for talking to the sqlite database
     * and initialises organisation view to update what the user sees.
     * @param connection the sqlite connection
     */
    public OrganisationController(Connection connection){
        organisationRepository = new OrganisationRepository(connection);
        organisationView = new OrganisationView();
    }


    /**
     * Method to create an organisation following the ACs specified.
     * If successful, creates a valid organisation based on user input and stores it into the database
     * An organisation is valid if it's name is not empty and it's name is not
     * a duplicate of an existing organisation
     * @param scanner the scanner to receive user input
     */
    public void createOrganisation(Scanner scanner){
        String organisationName = organisationView.getOrganisationDetails(scanner);
        Organisation organisation = new Organisation(organisationName);
        if(organisation.getOrganisationName().isEmpty()){
            organisationView.printEmptyMessage();
        }
        else if(!organisation.isValidOrganisationName(organisationRepository)){
            organisationView.printDuplicateNameErrorMessage();
        }
        else if(!organisation.insertOrganisation(organisationRepository)){
                organisationView.printFailureMessage();
            }
            else{
                organisationView.printSuccessMessage();
            }
    }

    /**
     * Method to get an organisation from a user based on their input
     * If there are no organisations in the database then returns null
     * @param scanner the scanner to check for user input
     * @return the organisation the user selects through their input,
     * null if there are no organisations in the database
     */
    public Organisation getOrganisationFromUser(Scanner scanner){
        List<Organisation> organisationList = organisationRepository.getOrganisations();
        if(organisationList.size() == 0){
            return null;
        }
        else {
            organisationView.printOrganisations(organisationList);
            String organisationName = organisationView.getOrganisationNameFromUser(scanner);
            Organisation organisation = organisationRepository.getOrganisationByName(organisationName);
            while (organisation == null) {
                organisationView.printInvalidOrganisationNameMessage();
                organisationName = organisationView.getOrganisationNameFromUser(scanner);
                organisation = organisationRepository.getOrganisationByName(organisationName);
            }
            return organisation;
        }
    }

    public OrganisationRepository getOrganisationRepository(){
        return organisationRepository;
    }
}
