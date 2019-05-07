package models;

/**
 * Model class for discourses
 * Used to create arguments from its content
 */
public class Discourse {

    /**
     * The ID of the discourse
     */
    private int id;

    /**
     * The content of the discourse
     */
    private String content;

    /**
     * Constructor for the discourse
     */
    public Discourse(){}

    /**
     * Overload constructor for the discourse. Takes a discourse ID and content
     * @param id the discourse id
     * @param content the content of the discourse
     */
    public Discourse(int id, String content){
        this.id = id;
        this.content = content;
    }

    public int getId(){
        return this.id;
    }
    public String getContent(){
        return this.content;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setContent(String content){
        this.content = content;
    }

}
