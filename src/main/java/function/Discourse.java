package function;

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




}
