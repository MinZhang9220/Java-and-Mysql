package datahandling;

import models.Organisation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganisationRepository {

    private Connection connection;

    public OrganisationRepository(Connection connection){
        this.connection = connection;
    }

    /**
     * Method to insert an organisation with a given organisation name in the database.
     * Returns true if the organisation is successfully inserted, false otherwise
     * @param organisationName the organisation's name
     * @return true if the organisation is successfully inserted, false otherwise
     */
    public boolean insertOrganisation(String organisationName){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into organisation(name) values (?)");
            preparedStatement.setString(1,organisationName);
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            if(count > 0){
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to check if an organisation with the given name
     * already exists in the sqlite database.
     * @param organisationName the organisation's name
     * @return true if it already exists, false otherwise
     */
    public boolean isDuplicateOrganisation(String organisationName){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("select * from organisation where name = ?");
            preparedStatement.setString(1,organisationName);
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                resultSet.close();
                return true;
            } else {
                resultSet.close();
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get a list with Organisations in it.
     * @return organisationList or null if there are no organisations in database.
     */
    public List<Organisation> getOrganisations(){
        try {
            PreparedStatement statement = connection.prepareStatement("select * from organisation");
            ResultSet result = statement.executeQuery();
            List<Organisation> organisationList = new ArrayList<>();
            while (result.next()) {
                Organisation organisation = new Organisation(result.getString("name"));
                organisationList.add(organisation);
            }
            return organisationList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a particular organisation when the user type an organisation name form keyboard.
     * @param organisationName
     * @return organisation or null if there is no organisation with the given name.
     */
    public Organisation getOrganisationByName(String organisationName){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from organisation where name = ?");
            preparedStatement.setString(1,organisationName);
            preparedStatement.closeOnCompletion();
            ResultSet result = preparedStatement.executeQuery();
            Organisation organisation = null;
            while (result.next()) {
                organisation = new Organisation(result.getString("name"));
            }
            return organisation;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
