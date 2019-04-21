package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class Argument {


    public static ArrayList<Discourse> discoursesArray = Discourse.discourses;

    protected String content;
    protected int discourseid;
    protected int startindices;
    protected int endindices;
     ;

    public Argument(){}

    public Argument(String content,int discourseid,int startindices,int endindices){
        this.content = content;
        this.discourseid = discourseid;
        this.startindices = startindices;
        this.endindices = endindices;
    }
    public String getContent(){ return this.content;}
    public int getDiscourseid(){return this.discourseid;}
    public int getStartindices(){return this.startindices;}
    public int getEndindices(){return this.endindices;}
    public ArrayList<Discourse> getDiscourses(){return this.discoursesArray;}

    public void setContent(String content){this.content = content;}
    public void setDiscourseid(int discourseid){this.discourseid = discourseid;}
    public void setStartindices(int startindices){this.startindices = startindices;}
    public void setEndindices(int endindices){this.endindices = endindices;}
    public void setDiscourses(ArrayList<Discourse> discourses){this.discoursesArray = discourses;}


    /**
     * Find discourse by discourse id
     * @param discourseid
     * @return discourse
     */

    public Discourse getDiscourseById(int discourseid){

        Discourse discourse = new Discourse();

        for(Discourse targetDiscourse:this.discoursesArray){
            if(targetDiscourse.getId() == discourseid){
                discourse.setDiscourseContent(targetDiscourse.getDiscourseContent());
                discourse.setId(targetDiscourse.getId());
                discourse.setDiscourse_theme(targetDiscourse.getDiscourse_theme());
                break;
            } else{
                continue;
            }
        }

        return discourse;
    }


    /**
     * Check if the start indices or the endindices is valid.
     * @param discourseid
     * @param startindices
     * @param endindices
     * @return result
     */
    public String[] validIndex(int discourseid,int startindices,int endindices){

        String[] validResult = new String[2];

        if(startindices < 0 || endindices > this.getDiscourseById(discourseid).getDiscourseContent().length() - 1 || startindices > endindices){
            /**
             * start indices must greater or equal than 0
             * end indices must less than or equal the length - 1 of the content
             * start indices must less than or equal to the end indices
             */
            if(startindices < 0){
                validResult[0] = "false";
                validResult[1] = "Start indices is wrong";
                System.out.println("Start indices must greater or equal than 0");

            } else if(endindices > this.getDiscourseById(discourseid).getDiscourseContent().length() - 1 ){
                validResult[0] = "false";
                validResult[1] = "End indices is wrong";
                System.out.println("End indices must less than or equal the length - 1 of the content");

            } else if(startindices > endindices){
                validResult[0] = "false";
                validResult[1] = "Indices is wrong";
                System.out.println("Start indices must less than or equal to the end indices");
            }
        } else{
            validResult[0] = "true";
            validResult[1] = "valid";
        }

        return validResult;
    }

    /**
     * Check if the argument is identical
     * @param discourseid
     * @param startindices
     * @param endindices
     * @oaran connection
     * @return argumentValidResult
     */
    public boolean validExistInDatabase(int discourseid, int startindices, int endindices, Connection connection){

        boolean argumentValidResult;

        assert null != connection : "connection can not be null!";
        if(this.validIndex(discourseid,startindices,endindices)[0] == "true"){

            try{

                PreparedStatement preparedStatement = connection.prepareStatement("select * from argument where discourseid=? and startindices=? and endindices=?");
                preparedStatement.setInt(1,discourseid);
                preparedStatement.setInt(2,startindices);
                preparedStatement.setInt(3,endindices);

                ResultSet resultSet = preparedStatement.executeQuery();
                preparedStatement.closeOnCompletion();

                if(resultSet.next()){
                    argumentValidResult = true;
                } else{
                    argumentValidResult = false;
                }

            } catch (SQLException e){
                System.out.println(e.getMessage());
                argumentValidResult = false;
            }

        } else{
            System.out.println(this.validIndex(discourseid,startindices,endindices)[1]);
            argumentValidResult = false;
        }

        return argumentValidResult;
    }


    /**
     * Create an argument
     * @param discourseid
     * @param startindices
     * @param endindices
     * @return executeResult
     */

    public String[] createArgument(int discourseid,int startindices,int endindices,Connection connection){

        String[] executeResult = new String[2];

        try{

            if(!this.validExistInDatabase(discourseid,startindices,endindices,connection)){

                String content = this.getDiscourseById(discourseid).getDiscourseContent().substring(startindices,endindices+1);

                PreparedStatement preparedStatement = connection.prepareStatement("insert into argument(discourseid, startindices, endindices, content) values (?,?,?,?)");
                preparedStatement.setInt(1,discourseid);
                preparedStatement.setInt(2,startindices);
                preparedStatement.setInt(3,endindices);
                preparedStatement.setString(4,content);
                int rowUpdate = preparedStatement.executeUpdate();
                //preparedStatement.closeOnCompletion();

                if(rowUpdate == 1){

                    executeResult[0] = "true";
                    executeResult[1] = "Success";
                } else{

                    executeResult[0] = "false";
                    executeResult[1] = "Fail";
                }

            } else {

                executeResult[0] = "false";
                executeResult[1] = "Fail";
            }

        } catch (SQLException e){

            System.out.println(e.getMessage());
            executeResult[0] = "123";
            executeResult[1] = "Fail";

        }
        System.out.printf("Create Argument result: %s\n", executeResult[1]);
        return executeResult;
    }
}
