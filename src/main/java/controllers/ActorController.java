package controllers;

import datahandling.ActorRepository;
import models.Actor;
import views.ActorView;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class ActorController {
    private ActorRepository actorRepository;
    private ActorView actorView;

    public ActorController(Connection connection){
        actorRepository = new ActorRepository(connection);
        actorView = new ActorView();
    }



    public void createActor(Scanner scanner){
        String[] actorDetails = actorView.getActorDetails(scanner);
        String firstName = actorDetails[0];
        String lastName = actorDetails[1];
        String levelOfTrust = actorDetails[2];
        Actor actor = new Actor(firstName, lastName);
        while(!actor.setLevelOfTrust(levelOfTrust)){
            levelOfTrust = actorView.getLevelOfTrust(scanner);
        }
        if(actor.getFirstName().isEmpty()){
            actorView.printFirstNameEmptyMessage();
        }
        else if(actor.getLastName().isEmpty()){
            actorView.printLastNameEmptyMessage();
        }
        else if(actor.isActorHomonym(actorRepository)){
            List<Actor> homonymActors = actor.getHomonymActors(actorRepository);
            String confirmationAnswer = "undefined";
            while(!confirmationAnswer.equalsIgnoreCase("yes")
            && !confirmationAnswer.equalsIgnoreCase("no")){
                confirmationAnswer = actorView.printHomonymConfirmMessage(homonymActors, scanner);
            }
            if(confirmationAnswer.equalsIgnoreCase("no")){
                actorView.printFailureMessage();
            }
            else if(!actor.insertActorIntoDatabase(actorRepository)){
                actorView.printFailureMessage();
            }
            else{
                actorView.printSuccessMessage();
            }
        }
        else if(!actor.insertActorIntoDatabase(actorRepository)){
            actorView.printFailureMessage();
        }
        else{
            actorView.printSuccessMessage();
        }
    }
}
