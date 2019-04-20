package datahandling;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
