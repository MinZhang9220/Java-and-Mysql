package controllers;

import datahandling.SQLiteJDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ApplicationManager {

    public static void main(String[] args) {
        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();

        Connection connection;
        if(args != null){
            if(args[0].equalsIgnoreCase("test")){
                connection = sqLiteJDBC.getConnectionToDatabase("test");
            }
            else{
                connection = sqLiteJDBC.getConnectionToDatabase("main");
            }
        }
        else{
            connection = sqLiteJDBC.getConnectionToDatabase("main");
        }


        OrganisationController organisationController = new OrganisationController(connection);

        ActorController actorController = new ActorController(connection);

        DiscourseController discourseController = new DiscourseController();

        ArgumentController argumentController = new ArgumentController(connection, discourseController);

        AffiliationController affiliationController =
                new AffiliationController(connection, actorController, organisationController);
        actorController.setAffiliationController(affiliationController);

        System.out.println("Welcome to Discourse System");
        System.out.println("----------------------------");
        System.out.println();
        System.out.println();
        System.out.println();


        System.out.println("-----------------------------------------");
        System.out.println("|  CommandCode | Command Content        |");
        System.out.println("-----------------------------------------");
        System.out.println("|        1     | Create organisation    |");
        System.out.println("-----------------------------------------");
        System.out.println("|        2     | Create Actor           |");
        System.out.println("-----------------------------------------");
        System.out.println("|        3     | Register an affiliation|");
        System.out.println("-----------------------------------------");
        System.out.println("|        4     | Register an argument   |");
        System.out.println("-----------------------------------------");
        System.out.println("|        q     | Quit                   |");
        System.out.println("-----------------------------------------");


        ArrayList<String> commandArray = new ArrayList<String>() {
            {
                add("1");
                add("2");
                add("3");
                add("4");
                add("q");
            }
        };


        Scanner scanner = new Scanner(System.in);
        boolean quitDiscourseSystem = false;
        String commandCode;
        while (!quitDiscourseSystem) {
            boolean validCommandCode = false;
            do {
                System.out.println("Type your command code");
                commandCode = scanner.nextLine();
                if (commandArray.contains(commandCode)) {
                    validCommandCode = true;
                } else {
                    System.out.println("Invalid command code");
                }
            } while (!validCommandCode);
            switch (commandCode) {
                case "1":
                    organisationController.createOrganisation(scanner);
                    break;
                case "2":
                    actorController.createActor(scanner);
                    break;
                case "3":
                    affiliationController.createAffiliation(scanner);
                    break;
                case "4":
                    argumentController.createArgument(scanner);
                    break;
                case "q":
                    quitDiscourseSystem = true;
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        System.out.println("Exiting Application");
    }
}