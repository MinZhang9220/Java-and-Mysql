package controllers;

import datahandling.ArgumentRepository;
import models.Argument;
import models.Discourse;
import views.ArgumentView;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class ArgumentController {

    private DiscourseController discourseController;
    private ArgumentRepository argumentRepository;
    private ArgumentView argumentView;

    /**
     * Constructor
     * @param connection
     * @param discourseController
     */
    public ArgumentController(Connection connection, DiscourseController discourseController){
        this.discourseController = discourseController;
        this.argumentRepository = new ArgumentRepository(connection);
        this.argumentView = new ArgumentView();
    }

    /**
     * Create an argument,all the related information get from user input.
     * @param scanner
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
     * Validating start indices and end indices which get from the user input.
     * @param scanner
     * @param argument
     * @return startEndIndices
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
