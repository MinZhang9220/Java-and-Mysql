package controllers;

import datahandling.ArgumentRepository;
import models.Argument;
import models.Discourse;
import views.ArgumentView;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

/**
 * The argument controller uses the argument model and view to perform argument functionality,
 * which is currently adding arguments into the system.
 */
public class ArgumentController {

    /**
     * The discourse controller used to link an argument to a discourse
     */
    private DiscourseController discourseController;
    /**
     * The argument repository used to make queries to the database
     */
    private ArgumentRepository argumentRepository;

    /**
     * The argument view used to receive input and provide feedback to the user on their actions
     */
    private ArgumentView argumentView;

    /**
     * Constructor for the argument controller class.
     * Initialises the argument repository for talking to the sqlite database
     * and initialises argument view to update what the user sees.
     * @param connection the sqlite connection
     * @param discourseController the discourse controller used to link an argument to a discourse
     */
    public ArgumentController(Connection connection, DiscourseController discourseController){
        this.discourseController = discourseController;
        this.argumentRepository = new ArgumentRepository(connection);
        this.argumentView = new ArgumentView();
    }

    /**
     * Method to create an argument following the ACs specified.
     * If successful, creates a valid argument based on user input and stores it into the database
     * @param scanner the scanner to receive user input
     */
    public void createArgument(Scanner scanner){
        Discourse discourse = discourseController.getDiscourseFromUser(scanner);
        if(discourse == null){
            argumentView.printNoDiscoursesMessage();
        }
        else {
            Argument argument = new Argument(discourse);
            List<Integer> startEndIndices = getValidStartEndIndices(scanner, argument);
            argument.setStartIndex(startEndIndices.get(0));
            argument.setEndIndex(startEndIndices.get(1));
            String rephrasing = argumentView.getRephrasingFromUser(scanner);
            while (!argument.isValidRephrasing(rephrasing)) {
                //print invalid rephrasing message
                rephrasing = argumentView.getRephrasingFromUser(scanner);
            }
            argument.setRephrasing(rephrasing);
            if (!argument.insertArgumentIntoDatabase(argumentRepository)) {
                argumentView.printFailureMessage();
            } else {
                argumentView.printSuccessMessage();
            }
        }
    }

    /**
     * Method to get valid start end indices from a discourse's content.
     * A start index is valid if it begins after a sentence ending punctuation mark (?,!,.)
     * and is shifted to the left to the nearest non white-space character.
     * An end index is valid if it is on a sentence ending punctuation mark (?,!,.)
     * and is also shifted to the left to the nearest non white-space character.
     * A start-end index is valid if there are no duplicate start-end indices in the database.
     * @param scanner the scanner to receive user input
     * @param argument the argument with a discourse's content to check for
     * @return an integer list of the start-end indices of size two.
     * The first index is the start index and the second index is the end index
     */
    public List<Integer> getValidStartEndIndices(Scanner scanner, Argument argument){
        List<Integer> startEndIndices = argumentView.getIndices(scanner);
        Integer startIndex = startEndIndices.get(0);
        Integer endIndex= startEndIndices.get(1);
        boolean startEndValid = false;
        while(!startEndValid){
            startIndex = argument.isValidStartIndex(startIndex);
            endIndex = argument.isValidEndIndex(endIndex);
            if(startIndex == -1){
                //print non valid start index message
                argumentView.printInvalidStartIndexMessage();
            }
            else if(endIndex  == -1){
                //print non valid end index message
                argumentView.printInvalidEndIndexMessage();
            }
            else if(!argument.hasNoDuplicates(startIndex,endIndex,argumentRepository)){
                //print duplicated argument message
                argumentView.printDuplicateMessage();
            }
            else{
                startEndValid = true;
            }
            if(!startEndValid){
                startEndIndices = argumentView.getIndices(scanner);
                startIndex = startEndIndices.get(0);
                endIndex= startEndIndices.get(1);
            }
            else{
                startEndIndices.set(0, startIndex);
                startEndIndices.set(1, endIndex);
            }
        }
        return startEndIndices;
    }

    public ArgumentRepository getArgumentRepository() {
        return argumentRepository;
    }
}
