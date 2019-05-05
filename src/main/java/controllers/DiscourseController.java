package controllers;

import datahandling.DiscourseRepository;
import models.Discourse;
import views.DiscourseView;

import java.util.List;
import java.util.Scanner;

/**
 * The discourse controller uses the discourse model and view to perform discourse functionality,
 * which is currently retrieving discourses from the system to add arguments
 */
public class DiscourseController {

    /**
     * The discourse repository used to retrieve discourses
     */
    private DiscourseRepository discourseRepository;

    /**
     * The discourse view used to receive input and provide feedback to the user on their actions
     */
    private DiscourseView discourseView;


    /**
     * Constructor for the argument controller class.
     * Initialises the discourse repository to retrieve discourses
     * and initialises discourse view to update what the user sees.
     */
    public DiscourseController(boolean isTestDatabase){
        discourseRepository = new DiscourseRepository(isTestDatabase);
        discourseView = new DiscourseView();
    }

    /**
     * Method to get a discourse from the discourse repository based on user input.
     * @param scanner the scanner to receive user input
     * @return the discourse that the user chooses
     */
    public Discourse getDiscourseFromUser(Scanner scanner){
        List<Discourse> discourseList = discourseRepository.getDiscourses();
        if(discourseList.size() == 0){
            return null;
        }
        else {
            discourseView.printDiscourses(discourseList);
            Integer id = discourseView.getDiscourseIdFromUser(scanner);
            Discourse discourse = discourseRepository.getDiscourseById(id);
            while (discourse == null) {
                discourseView.printInvalidDiscourseIdMessage();
                id = discourseView.getDiscourseIdFromUser(scanner);
                discourse = discourseRepository.getDiscourseById(id);
            }
            return discourse;
        }
    }

    public DiscourseRepository getDiscourseRepository(){
        return discourseRepository;
    }
}