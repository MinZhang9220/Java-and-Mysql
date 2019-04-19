package function;

import java.util.ArrayList;

public class Argument {

    protected String content;
    protected int discourseid;
    protected int startindices;
    protected int endindices;
    ArrayList<Discourse> discourses;

    public Argument(){}
    public Argument(String content,int discourseid,int startindices,int endindices){
        this.content = content;
        this.discourseid = discourseid;
        this.startindices = startindices;
        this.endindices = endindices;
        this.discourses = new ArrayList<>();
    }
    public String getContent(){ return this.content;}
    public int getDiscourseid(){return this.discourseid;}
    public int getStartindices(){return this.startindices;}
    public int getEndindices(){return this.endindices;}
    public ArrayList<Discourse> getDiscourses(){return this.discourses;}

    public void setContent(String content){this.content = content;}
    public void setDiscourseid(int discourseid){this.discourseid = discourseid;}
    public void setStartindices(int startindices){this.startindices = startindices;}
    public void setEndindices(int endindices){this.endindices = endindices;}
    public void setDiscourses(ArrayList<Discourse> discourses){this.discourses = discourses;}







}
