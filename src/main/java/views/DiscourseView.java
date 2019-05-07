package views;

import models.Discourse;

import java.util.List;
import java.util.Scanner;

/**
 * View class for a discourse. Used to provide feedback and receive input from a user
 * when they interact with functionality related to discourses
 */
public class DiscourseView {


    /**
     * Prints a list of discourses in a nicely formatted manner, displaying their content, start index and end index
     * @param discourseList the list of discourses to be printed
     */
    public void printDiscourses(List<Discourse> discourseList){
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        System.out.printf("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        System.out.printf("%s%-150s%s%12s%s%12s%s%5s%s\n","|","Content","|","Start index","|","End index","|","id","|");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");


        for(Discourse discourse : discourseList){
            System.out.printf("%s%-150s%s%12d%s%12d%s%5d%s\n","|",discourse.getContent(),"|",0,"|",discourse.getContent().length() - 1,"|",discourse.getId(),"|");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }

    /**
     * Gets a potential discourse ID based on user input.
     * If the user does not input an integer they receive an error message and are prompted to try again
     * @param scanner the scanner to receive user input
     * @return the discourse id
     */
    public Integer getDiscourseIdFromUser(Scanner scanner){
        System.out.println("Enter a discourse id: ");
        while (!scanner.hasNextInt()) {
            String input = scanner.next();
            System.out.printf("\"%s\" is not a valid number.\n", input);
        }
        Integer id = scanner.nextInt();
        scanner.nextLine();
        return id;
    }

    /**
     * Prints a message to the user that the discourse id that they inputted was invalid
     */
    public void printInvalidDiscourseIdMessage(){
        System.out.println("Invalid discourse id entered. Please try again.");
    }
}
