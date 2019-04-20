package function;

import java.util.ArrayList;

/**
 * Sample data for discourse
 */
public class Discourse {

    protected int id;
    protected String discourseContent;
    protected String discourse_theme;

    public Discourse(){}

    public Discourse(int id, String discourseContent,String discourse_theme){
        this.id = id;
        this.discourseContent = discourseContent;
        this.discourse_theme = discourse_theme;
    }

    public int getId(){
        return this.id;
    }
    public String getDiscourseContent(){
        return this.discourseContent;
    }
    public String getDiscourse_theme(){return this.discourse_theme;}

    public void setId(int id){
        this.id = id;
    }
    public void setDiscourseContent(String discourseContent){
        this.discourseContent = discourseContent;
    }
    public void setDiscourse_theme(String discourse_theme){this.discourse_theme = discourse_theme;}


    public static String discourse_content_1 = "When he spoke to the Democratic National Convention in support of Senator John Kerry" +
            "the party’s presidential nominee against George W. Bush";


    public static String discourse_content_2 = "As Supreme Allied Commander in Europe," +
            "Gen Eisenhower announced the D-Day landings at Normandy to the people of France and Western Europe.";
    public static String discourse_content_3 = "Delivered in secret before a rapt audience of Communist apparatchiks," +
            "this remarkable speech by a Soviet leader helped destroy Stalin’s reputation. ";
    public static ArrayList<Discourse> discourses= new ArrayList<>();

    static{
        discourses.add(new Discourse(1,discourse_content_1,"The Audacity of Hope"));
        discourses.add(new Discourse(2,discourse_content_2,"D-Day broadcast to the people of Western Europe"));
        discourses.add(new Discourse(3,discourse_content_3,"Kruschev's Secret Speech"));
    }




}
