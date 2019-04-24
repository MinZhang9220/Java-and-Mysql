package models;

/**
 * Sample data for discourse
 */
public class Discourse {

    private int id;
    private String content;

    public Discourse(){}

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
