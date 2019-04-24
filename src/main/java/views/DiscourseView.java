package views;

import models.Discourse;

import java.util.List;
import java.util.Scanner;

public class DiscourseView {


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

    public void printInvalidDiscourseIdMessage(){
        System.out.println("Invalid discourse id entered. Please try again.");
    }
}
