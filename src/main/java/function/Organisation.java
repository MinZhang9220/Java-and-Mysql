/**
 * Organisation model
 *
 */

package function;

import java.sql.*;


public class Organisation {

    protected String organisationName;

    public Organisation(){}

    public Organisation(String organisationName){
        this.organisationName = organisationName;
    }

    public void setOrganisationName(String organisationName){
        this.organisationName = organisationName;
    }

    public String getOrganisationName(){
        return this.organisationName;
    }


    public boolean validOrganisationName(String organisationName,Connection connection){

        boolean validOrganisationName = true;
        assert null != connection && organisationName != null : "connection and organisation name can not be null!";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("select * from organisation where name = ?");
            preparedStatement.setString(1,organisationName);
            ResultSet resultSet = preparedStatement.executeQuery();
            preparedStatement.closeOnCompletion();

            if(resultSet.next()){
                validOrganisationName = false;
                System.out.println("This organisation name already exists in database.");
            } else {
                validOrganisationName = true;
            }


        } catch (SQLException e){
            System.out.println(e.getMessage());
            validOrganisationName = false;
        }

        return validOrganisationName;
    }


    /**
     * Create an organisation if the organisation name is valid.
     * @param organisationName
     * @param connection
     * @return confirm_information
     */
    public String[] createOrganisation(String organisationName, Connection connection){

        String[] insertResult = new String[2];

        assert null != connection && organisationName != null : "connection and organisation name can not be null!";
        try {

            if(this.validOrganisationName(organisationName,connection)){
                PreparedStatement preparedStatement = connection.prepareStatement("insert into organisation(name) values (?)");
                preparedStatement.setString(1,organisationName);
                preparedStatement.executeUpdate();
                preparedStatement.closeOnCompletion();
                insertResult[0] = "true";
                insertResult[1] = "Success";
            } else{
                insertResult[0] = "false";
                insertResult[1] = "Fail";
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
            //System.out.println(e.getSQLState());
            insertResult[0] = "false";
            insertResult[1] = "Fail";

        }
        System.out.printf("Create Organisation result: %s", insertResult[1]);
        return insertResult;
    }







}

