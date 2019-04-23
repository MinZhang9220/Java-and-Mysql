package controllers;

import datahandling.DiscourseRepository;
import models.Discourse;
import views.DiscourseView;

import java.util.List;
import java.util.Scanner;


public class DiscourseController {

    private DiscourseRepository discourseRepository;

    private DiscourseView discourseView;


    public DiscourseController(boolean isTestDatabase){
        discourseRepository = new DiscourseRepository(isTestDatabase);
        discourseView = new DiscourseView();
    }

    public Discourse getDiscourseFromUser(Scanner scanner){
        List<Discourse> discourseList = discourseRepository.getDiscourses();
        discourseView.printDiscourses(discourseList);
        Integer id = discourseView.getDiscourseIdFromUser(scanner);
        Discourse discourse = discourseRepository.getDiscourseById(id);
        while(discourse == null) {
            discourseView.printInvalidDiscourseIdMessage();
            id = discourseView.getDiscourseIdFromUser(scanner);
            discourse = discourseRepository.getDiscourseById(id);
        }
        return discourse;
    }
}