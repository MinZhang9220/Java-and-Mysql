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

public class AffiliationRepository {

    private Connection connection;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
