package controllers;

import datahandling.OrganisationRepository;
import models.Organisation;
import views.OrganisationView;

import java.sql.Connection;
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
}
