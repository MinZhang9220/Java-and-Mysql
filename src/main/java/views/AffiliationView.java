package views;

import java.util.Scanner;

public class AffiliationView {

    public String getRole(Scanner scanner){
        System.out.println("Enter a role: (leave blank for no role)");
        return scanner.nextLine();
    }

    /**
     * Gets the start and end dates of an affiliation, guiding the user through with print statements.
     * @param scanner the scanner used to receive user input
     * @return the string array with the results provided by the user
     */
    public String[] getStartEndDates(Scanner scanner){
        System.out.println("Please type the affiliation's start date using the following format: yyyy-MM-dd (leave blank for none)");
        String startDate = scanner.nextLine();
        System.out.println("Please type the affiliation's end date using the following format: yyyy-MM-dd (leave blank for none)");
        String endDate = scanner.nextLine();
        String[] startEndDetails = {startDate, endDate};
        return startEndDetails;
    }

    /**
     * Prints a failure message when the affiliation is
     * unsuccessfully inserted into the database.
     */
    public void printFailureMessage(){
        System.out.println("The insert operation was unsuccessful.");
    }

    /**
     * Prints a failure message when the start date is
     * in an invalid format that doesn't follow yyyy-MM-dd
     */
    public void printInvalidStartDateMessage(){
        System.out.println("Invalid start date format. The start date should follow the format yyyy-MM-dd");
    }

    /**
     * Prints a failure message when the end date is
     * in an invalid format that doesn't follow yyyy-MM-dd
     */
    public void printInvalidEndDateMessage(){
        System.out.println("Invalid end date format. The end date should follow the format yyyy-MM-dd");
    }

    /**
     * Prints a failure message when the end date is prior to the start date.
     */
    public void printInvalidStartEndDateMessage(){
        System.out.println("The end date is prior to the start date. The end date has to be after the start date.");
    }

    /**
     * Prints a success message that the affiliation was
     * successfully inserted into the database.
     */
    public void printSuccessMessage(){
        System.out.println("The affiliation was successfully inserted into the database.");
    }

    /**
     * Prints a message that there aren't any organisations in the database, and thus an affiliation cannot be created.
     */
    public void printNoOrganisationsMessage(){
        System.out.println("There are no organisations in the database. Please create some first!");
    }

    /**
     * Prints a message that there aren't any actors in the database, and thus an affiliation cannot be created.
     */
    public void printNoActorsMessage(){
        System.out.println("There are no actors in the database. Please create some first!");
    }
}
