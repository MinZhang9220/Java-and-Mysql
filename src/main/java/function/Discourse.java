package function;

/**
 * Sample data for discourse
 */
public class Discourse {

    protected int id;
    protected String discourseContent;
    protected String discourse_theme;

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

    public void setId(int id){
        this.id = id;
    }
    public void setDiscourseContent(String discourseContent){
        this.discourseContent = discourseContent;
    }




}
