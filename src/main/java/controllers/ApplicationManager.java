package controllers;

import models.Discourse;
import datahandling.SQLiteJDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApplicationManager {


    public List<Discourse> initialiseDiscourse(){
        /**
         * Fake a database of Discourses
         */

        String discourse_content_1 = "When he spoke to the Democratic National Convention in support of Senator John Kerry" +
                "the party’s presidential nominee against George W. Bush";


        String discourse_content_2 = "As Supreme Allied Commander in Europe," +
                "Gen Eisenhower announced the D-Day landings at Normandy to the people of France and Western Europe.";
        String discourse_content_3 = "Delivered in secret before a rapt audience of Communist apparatchiks," +
                "this remarkable speech by a Soviet leader helped destroy Stalin’s reputation. ";

        List<Discourse> discourses = new ArrayList<>();
        discourses.add(new Discourse(1,discourse_content_1,"The Audacity of Hope"));
        discourses.add(new Discourse(2,discourse_content_2,"D-Day broadcast to the people of Western Europe"));
        discourses.add(new Discourse(3,discourse_content_3,"Kruschev's Secret Speech"));

        return discourses;
    }

    public static void main(String [ ] args) {
        //Initialise discourses
        ApplicationManager applicationManager = new ApplicationManager();
        List<Discourse> discourses = applicationManager.initialiseDiscourse();

        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
        Connection connection = sqLiteJDBC.getConnectionToDatabase();

        OrganisationController organisationController = new OrganisationController(connection);
        ActorController actorController = new ActorController(connection);

        boolean quitDiscourseSystem = false;

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


        ArrayList<String> commandArray = new ArrayList<String>(){
            {
                add("1");
                add("2");
                add("3");
                add("4");
                add("q");
            }
        };


        Scanner scanner = new Scanner(System.in);

        while (!quitDiscourseSystem){

            String commandCode;

            boolean validCommandCode = false;

            do{
                System.out.println("Type your command code");
                commandCode = scanner.nextLine();
                if(commandArray.contains(commandCode)){
                    validCommandCode = true;
                } else{
                    System.out.println("Invalid command code");
                }
            } while(!validCommandCode);

            if(commandCode.equals("1")){
                organisationController.createOrganisation(scanner);
            } else if(commandCode.equals("2")){
                actorController.createActor(scanner);
            } else if(commandCode.equals("3")){
                //do something
            } else if(commandCode.equals("4")){
                //Register an argument
                Argument argument = new Argument();

                System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

                System.out.printf("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

                System.out.printf("%s%-150s%s%12s%s%12s%s%5s%s\n","|","Content","|","Start indices","|","End indices","|","id","|");
                System.out.println();
                System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");


                for(Discourse discourse : discourses){
                    System.out.printf("%s%-150s%s%12d%s%12d%s%5d%s\n","|",discourse.getDiscourseContent(),"|",0,"|",discourse.getDiscourseContent().length() - 1,"|",discourse.getId(),"|");
                    System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                }
                System.out.println("Discourse id: ");
                Scanner commandScannerForArguments = new Scanner(System.in);
                int discourseId = commandScannerForArguments.nextInt();
                System.out.println("Start Indices: ");
                int startIndices = commandScannerForArguments.nextInt();
                System.out.println("End Indices: ");
                int endIndices = commandScannerForArguments.nextInt();

                argument.createArgument(discourseId,startIndices,endIndices,sqLiteJDBC.getConnectionToDatabase());



            } else if(commandCode.equals("q")){
                //Quit this system
                quitDiscourseSystem = true;
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}