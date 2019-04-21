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

    public ArgumentController(Connection connection, DiscourseController discourseController){
        this.discourseController = discourseController;
        this.argumentRepository = new ArgumentRepository(connection);
        this.argumentView = new ArgumentView();
    }

    public void createArgument(Scanner scanner){
        Discourse discourse = discourseController.getDiscourseFromUser(scanner);
        Argument argument = new Argument(discourse);
        List<Integer> startEndIndices = argumentView.getIndices(scanner);
        Integer startIndex = startEndIndices.get(0);
        Integer endIndex= startEndIndices.get(1);
        boolean startEndValid = false;
        while(!startEndValid){
            if(!argument.isValidStartIndex(startIndex)){
                //print non valid start index message
                argumentView.printInvalidStartIndexMessage();
            }
            else if(!argument.isValidEndIndex(endIndex)){
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
        }
        argument.setStartIndex(startIndex);
        argument.setEndIndex(endIndex);
        String rephrasing = argumentView.getRephrasingFromUser(scanner);
        while(!argument.isValidRephrasing(rephrasing)){
            //print invalid rephrasing message
            rephrasing = argumentView.getRephrasingFromUser(scanner);
        }
        argument.setRephrasing(rephrasing);
        if(!argument.insertArgumentIntoDatabase(argumentRepository)){
            argumentView.printFailureMessage();
        }
        else{
            argumentView.printSuccessMessage();
        }
    }
}
