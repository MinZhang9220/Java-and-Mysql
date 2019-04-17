package function;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class ApplicationManager {


    public static void main(String [ ] args) {

        boolean quitDiscourseSystem = false;
        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
        Connection connection = sqLiteJDBC.getConnectionToDatabase();

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
        System.out.println("|        5     | Quit application       |");
        System.out.println("-----------------------------------------");

        ArrayList<Integer> commandArray = new ArrayList<Integer>(){
            {
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
            }
        };


        Scanner scanner = new Scanner(System.in);
        while (!quitDiscourseSystem){

            int commandCode;

            do{
                System.out.println("Type your command code");
                commandCode = scanner.nextInt();
                scanner.nextLine();
            } while(!commandArray.contains(commandCode));

            if(commandCode == 1){
                System.out.println("Please type your organisation name.");
                String organisationName = scanner.nextLine();
                Organisation organisation = new Organisation(organisationName);
                organisation.createOrganisation(organisationName,connection);
                organisation.printOrganisations(connection);
            }
            if(commandCode == 5){
                System.out.println("**Exiting application**");
                quitDiscourseSystem = true;
            }
        }
    }

}
