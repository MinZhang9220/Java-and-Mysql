package datahandling;

import models.Actor;
import models.Affiliation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The affiliation repository used to make queries to the database
 */
public class AffiliationRepository {

    /**
     * The SQL connection which is the database connection
     */
    private Connection connection;

    /**
     * Constructor for the affiliation repository
     * @param connection the SQL connection used to make queries to the database
     */
    public AffiliationRepository(Connection connection){
        this.connection = connection;
    }

    /**
     * Method to insert an affiliation with an actor, organisation, role, start date and end date into the database.
     * @param affiliation the affiliation to be inserted
     * @return true if the affiliation is successfully inserted, false otherwise
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

    /**
     * Methods to get a list of affiliations that are linked to a given actor.
     * @param actor the actor
     * @param organisationRepository the repository used to query the database to get an organisation
     * @return
     */
    public List<Affiliation> getAffiliationsByActor(Actor actor, OrganisationRepository organisationRepository){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from affiliation where actorid = ?");
            preparedStatement.setInt(1,actor.getActorid());
            preparedStatement.closeOnCompletion();
            ResultSet result = preparedStatement.executeQuery();
            List<Affiliation> affiliationList = new ArrayList<>();
            while (result.next()) {
                Affiliation affiliation = new Affiliation();
                affiliation.setActor(actor);
                affiliation.setOrganisation(organisationRepository.getOrganisationByName(
                        result.getString("organisationname")));
                affiliation.setRole(result.getString("role"));
                affiliation.setStartDate(result.getString("startdate"));
                affiliation.setEndDate(result.getString("enddate"));
                affiliationList.add(affiliation);
            }
            return affiliationList;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
