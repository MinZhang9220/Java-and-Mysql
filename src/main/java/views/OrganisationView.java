package views;

import models.Organisation;

import java.util.List;
import java.util.Scanner;

public class OrganisationView {
    /**
     * Prints the error message that a duplicated organisation name
     * was found in the database.
     */
    public void printDuplicateNameErrorMessage(){
        System.out.println("This organisation name already exists in database.");
    }

    /**
     * Prints a success message that the organisation was
     * successfully inserted into the database.
     */
    public void printSuccessMessage(){
        System.out.println("The organisation was successfully inserted into the database.");
    }

    /**
     * Prints a failure message when the organisation is
     * unsuccessfully inserted into the database.
     */
    public void printFailureMessage(){
        System.out.println("The insert operation was unsuccessful.");
    }

    /**
     * Prints a failure message when the given
     * organisation name is empty.
     */
    public void printEmptyMessage(){
        System.out.println("The organisation name is empty.");
    }

    /**
     *
     * @param scanner
     * @return
     */
    public String getOrganisationDetails(Scanner scanner){
        System.out.println("Please type your organisation name.");
        String organisationName = scanner.nextLine();
        return organisationName;
    }

    public void printOrganisations(List<Organisation> organisationList){
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("ORGANISATION NAMES");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for(Organisation organisation : organisationList){
            System.out.println(organisation.getOrganisationName());
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }

    public String getOrganisationNameFromUser(Scanner scanner){
        System.out.println("Enter an organisation name: ");
        return scanner.nextLine();
    }

    public void printInvalidOrganisationNameMessage(){
        System.out.println("Invalid organisation name entered. Please try again.");
    }
}
