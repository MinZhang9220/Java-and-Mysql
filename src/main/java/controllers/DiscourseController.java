package controllers;

import datahandling.DiscourseRepository;
import models.Discourse;
import views.DiscourseView;

import java.util.List;
import java.util.Scanner;


public class DiscourseController {

    private DiscourseRepository discourseRepository;

    private DiscourseView discourseView;


    /**
     * Constructor
     * @param isTestDatabase
     */
    public DiscourseController(boolean isTestDatabase){
        discourseRepository = new DiscourseRepository(isTestDatabase);
        discourseView = new DiscourseView();
    }

    /**
     * Get a discourse when user type discourse id from the keyboard
     * @param scanner
     * @return discourse
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