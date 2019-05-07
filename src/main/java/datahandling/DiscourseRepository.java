package datahandling;

import models.Discourse;

import java.util.ArrayList;
import java.util.List;

/**
 * The argument repository used to retrieve discourses
 */
public class DiscourseRepository {

    /**
     * The list of discourses which is a substitute for the discourses within a database
     */
    private List<Discourse> discourses= new ArrayList<>();

    /**
     * Constructor for the discourse repository
     * Currently doesn't query the database but is constructed using java strings instead, as creating discourses
     * isn't part of the stories.
     * @param isTestDatabase if true, get the content of the test database, otherwise the production database
     */
    public DiscourseRepository(boolean isTestDatabase){
        if(isTestDatabase) {
            //Populates the test database (this allows you to change the real database
            //without affecting the test database)
            String contentOne = "When he spoke to the Democratic National Convention in support of Senator John Kerry" +
                    "the party’s presidential nominee against George W. Bush";
            String contentTwo = "As Supreme Allied Commander in Europe," +
                    "Gen Eisenhower announced the D-Day landings at Normandy to the people of France and Western Europe.";
            String contentThree = "Delivered in secret before a rapt audience of Communist apparatchiks," +
                    "this remarkable speech by a Soviet leader helped destroy Stalin’s reputation. ";
            String contentFour = "   Sometimes I do feel like I'm a failure. Like there's no hope for me? " +
                    "But even so, I'm not gonna give up. Ever!";
            //start index 50/51 is valid
            String contentFive = "I have to work harder than anyone else to make it! I'll never catch up otherwise! " +
                    "I want to be like you! Like you. The strongest hero.   ";
            discourses.add(new Discourse(1, contentOne));
            discourses.add(new Discourse(2, contentTwo));
            discourses.add(new Discourse(3, contentThree));
            discourses.add(new Discourse(4, contentFour));
            discourses.add(new Discourse(5, contentFive));
        }
        else{
            //Populates the real database (this allows you to change the real database
            // without affecting the test database)
            String contentOne = "When he spoke to the Democratic National Convention in support of Senator John Kerry" +
                    "the party’s presidential nominee against George W. Bush. As Supreme Allied Commander in Europe," +
                    "Gen Eisenhower announced the D-Day landings at Normandy to the people of France and Western Europe." +
                    "Delivered in secret before a rapt audience of Communist apparatchiks," +
                    "this remarkable speech by a Soviet leader helped destroy Stalin’s reputation. ";
            String contentTwo = "I see this spark in you. It's amazing. What ever you choose to do with it, you'll be great. ";
            String contentThree = "Behind this mask there is more than just flesh. " +
                    "Beneath this mask there is an idea, and ideas are bulletproof.";
            String contentFour = "   Sometimes I do feel like I'm a failure. Like there's no hope for me. " +
                    "But even so, I'm not gonna give up. Ever!";
            //start index 50/51 is valid
            String contentFive = "I have to work harder than anyone else to make it! I'll never catch up otherwise! " +
                    "I want to be like you! Like you. The strongest hero.   ";
            discourses.add(new Discourse(1, contentOne));
            discourses.add(new Discourse(2, contentTwo));
            discourses.add(new Discourse(3, contentThree));
            discourses.add(new Discourse(4, contentFour));
            discourses.add(new Discourse(5, contentFive));
        }
    }

    public List<Discourse> getDiscourses() {
        return discourses;
    }

    public void setDiscourses(List<Discourse> discourseList) {
        discourses = discourseList;
    }

    /**
     * Find discourse by discourse id
     * @param discourseid
     * @return discourse
     */

    public Discourse getDiscourseById(int discourseid){
        for(Discourse discourse: discourses){
            if(discourse.getId() == discourseid){
                return discourse;
            }
        }
        return null;
    }
}
