package controllers;

import datahandling.OrganisationRepository;
import models.Organisation;
import views.OrganisationView;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class OrganisationController {


    private OrganisationRepository organisationRepository;
    private OrganisationView organisationView;

    public OrganisationController(Connection connection){
        organisationRepository = new OrganisationRepository(connection);
        organisationView = new OrganisationView();
    }



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

    public Organisation getOrganisationFromUser(Scanner scanner){
        List<Organisation> organisationList = organisationRepository.getOrganisations();
        organisationView.printOrganisations(organisationList);
        String organisationName = organisationView.getOrganisationNameFromUser(scanner);
        Organisation organisation = organisationRepository.getOrganisationByName(organisationName);
        while(organisation == null) {
            organisationView.printInvalidOrganisationNameMessage();
            organisationName = organisationView.getOrganisationNameFromUser(scanner);
            organisation = organisationRepository.getOrganisationByName(organisationName);
        }
        return organisation;
    }
}
