package function;

import java.util.ArrayList;
import java.util.Scanner;

public class ApplicationManager {


    public static void main(String [ ] args) {

        boolean quitDiscourseSystem = false;
        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();

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


        while (!quitDiscourseSystem){


            Scanner commandCodeScanner = new Scanner(System.in);


            String commandCode;
            boolean validCommandCode = false;

            do{

                System.out.println("Type your command code");
                commandCode = commandCodeScanner.next();
                if(commandArray.contains(commandCode)){
                    validCommandCode = true;
                } else{
                    System.out.println("Invalid command code");
                }
            } while(!validCommandCode);

            if(Integer.parseInt(commandCode) == 1){
                System.out.println("Please type your organisation name.");
                Scanner organisationNameScanner = new Scanner(System.in);
                String organisationName = organisationNameScanner.nextLine();
                Organisation organisation = new Organisation(organisationName);
                organisation.createOrganisation(organisationName,sqLiteJDBC.getConnectionToDatabase());
            } else if(Integer.parseInt(commandCode) == 2){
                //do something
            } else if(Integer.parseInt(commandCode) == 3){
                //do something
            } else if(Integer.parseInt(commandCode) == 4){
                //Register an argument

                Argument argument = new Argument();

                System.out.printf("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

                System.out.printf("%s%-150s%s%12s%s%12s%s%5s%s\n","|","Content","|","Start indices","|","End indices","|","id","|");
                System.out.println();
                System.out.printf("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");


                for(Discourse discourse:Discourse.discourses){
                    System.out.printf("%s%-150s%s%12d%s%12d%s%5d%s\n","|",discourse.getDiscourseContent(),"|",0,"|",discourse.getDiscourseContent().length() - 1,"|",discourse.getId(),"|");
                    System.out.printf("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
                }
                System.out.println("Discourse id: ");
                Scanner commandScannerForArguments = new Scanner(System.in);
                int discourseId = commandScannerForArguments.nextInt();
                System.out.println("Start Indices: ");
                int startIndices = commandScannerForArguments.nextInt();
                System.out.println("End Indices: ");
                int endIndices = commandScannerForArguments.nextInt();

                argument.createArgument(discourseId,startIndices,endIndices,sqLiteJDBC.getConnectionToDatabase());



            } else if(commandCode == "q"){
                //Quit this system
                quitDiscourseSystem = true;
            }
        }






    }

}