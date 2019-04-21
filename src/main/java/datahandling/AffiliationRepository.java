package datahandling;

import models.Affiliation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AffiliationRepository {

    private Connection connection;

    public AffiliationRepository(Connection connection){
        this.connection = connection;
    }

    /**
     * Method to insert an argument in the database.
     * @param affiliation the argument to be inserted
     * @return true if the argument is successfully inserted, false otherwise
     */
    public boolean insertAffiliation(Affiliation affiliation){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into affiliation(actorid,organisationname,role,startdate,enddate) values (?,?,?,?,?)");
            preparedStatement.setInt(1,affiliation.getActor().getActorid());
            preparedStatement.setString(2,affiliation.getOrganisation().getOrganisationName());
            preparedStatement.setString(3,affiliation.getRole());
            preparedStatement.setString(4,affiliation.getStartDateString());
            preparedStatement.setString(5,affiliation.getEndDateString());
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            return count > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
