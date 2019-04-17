package function;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String [ ] args) {

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

        ArrayList<Integer> commandArray = new ArrayList<Integer>(){
            {
                add(1);
                add(2);
                add(3);
                add(4);
            }
        };


        while (!quitDiscourseSystem){


            Scanner commandCodeScanner = new Scanner(System.in);


            int commandCode;

            do{
                System.out.println("Type your command code");
                commandCode = commandCodeScanner.nextInt();

            } while(!commandArray.contains(commandCode));

            if(commandCode == 1){
                System.out.println("Please type your organisation name.");
                SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
                Scanner organisationNameScanner = new Scanner(System.in);
                String organisationName = organisationNameScanner.nextLine();
                Organisation organisation = new Organisation(organisationName);
                organisation.createOrganisation(organisationName,sqLiteJDBC.connectionToDatabase());
            }
        }






    }

}
