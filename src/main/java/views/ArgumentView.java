package views;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * View class for an argument. Used to provide feedback and receive input from a user
 * when they interact with functionality related to arguments
 */
public class ArgumentView {

    /**
     * Gets the start and end indices of an argument based on the user input.
     * If the indices are not integers then the user is prompted with an error message and to try again
     * @param scanner the scanner to receive user input
     * @return an integer array where the first index is the start index and the second index is the end index
     */
    public List<Integer> getIndices(Scanner scanner){
        System.out.println("Enter a start index: ");
        while (!scanner.hasNextInt()) {
            String input = scanner.next();
            System.out.printf("\"%s\" is not a valid number.\n", input);
        }
        Integer startIndex = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter an end index: ");
        while (!scanner.hasNextInt()) {
            String input = scanner.next();
            System.out.printf("\"%s\" is not a valid number.\n", input);
        }
        Integer endIndex = scanner.nextInt();
        scanner.nextLine();
        List<Integer> startEndIndices = new ArrayList<>();
        startEndIndices.add(startIndex);
        startEndIndices.add(endIndex);
        return startEndIndices;
    }

    /**
     * Gets the argument rephrasing of an argument based on the user's input
     * @param scanner the scanner to receive user input
     * @return the rephrasing string
     */
    public String getRephrasingFromUser(Scanner scanner){
        System.out.println("Enter a rephrasing: ");
        return scanner.nextLine();
    }

    /**
     * Prints a message telling the user that the argument's start index is invalid.
     */
    public void printInvalidStartIndexMessage(){
        System.out.println("The argument's start index is invalid. A valid start index should be within the start and end of the discourse's content.");
    }

    /**
     * Prints a message telling the user that the argument's end index is invalid.
     */
    public void printInvalidEndIndexMessage(){
        System.out.println("The argument's end index is invalid. A valid end index should be within the start and end of the discourse's content.");
    }

    public void printDuplicateMessage(){
        System.out.println("An identical argument already exists in the database.");
    }

    /**
     * Prints a failure message when the argument is
     * unsuccessfully inserted into the database.
     */
    public void printFailureMessage(){
        System.out.println("The insert operation was unsuccessful.");
    }

    /**
     * Prints a success message that the argument was
     * successfully inserted into the database.
     */
    public void printSuccessMessage(){
        System.out.println("The argument was successfully inserted into the database.");
    }

    /**
     * Prints a message telling the user that there are no discourses in the database
     */
    public void printNoDiscoursesMessage(){
        System.out.println("There are no discourses in the database that can be used to extract an argument.");
    }
}
