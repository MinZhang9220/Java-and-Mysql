package steps;

import controllers.ApplicationManager;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import datahandling.SQLiteJDBC;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class AffiliationTestSteps {

    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;
    TestDatabaseManager testDatabaseManager = new TestDatabaseManager();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUp() {
        connection = sqLiteJDBC.getConnectionToDatabase("test");
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        testDatabaseManager.resetDatabase(connection);
    }

    @Given("There exists an actor with first name {string} and last name {string} in the database")
    public void thereExistsAnActorWithFirstNameAndLastNameInTheDatabase(String string, String string2) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into actor(firstname,lastname) values (?,?)");
            preparedStatement.setString(1,string);
            preparedStatement.setString(2,string2);
            preparedStatement.closeOnCompletion();
            preparedStatement.executeUpdate();
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Given("There exists an organisation with the name {string} in the database")
    public void thereExistsAnOrganisationWithTheNameInTheDatabase(String string) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into organisation(name) values (?)");
            preparedStatement.setString(1,string);
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            Assert.assertTrue(count > 0);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @When("I create an affiliation with the actor of first name Izuku and last name Midoriya with the organisation UA with role Hero and start date February eighteenth twenty seventeen and end date December twentieth twenty twenty using the file {string}")
    public void iCreateAnAffiliationWithTheActorOfFirstNameIzukuAndLastNameMidoriyaWithTheOrganisationUAWithRoleHeroAndStartDateFebruaryEighteenthTwentySeventeenAndEndDateDecemberTwentiethTwentyTwentyUsingTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The affiliation with actor of first name {string} and last name {string} and organisation {string} with role {string} and start date {string} and end date {string} is stored into the database")
    public void theAffiliationWithActorOfFirstNameAndLastNameAndOrganisationWithRoleAndStartDateAndEndDateIsStoredIntoTheDatabase(String string, String string2, String string3, String string4, String string5, String string6) {
        try {
            PreparedStatement statement = connection.prepareStatement("select actorid from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            int actorid = 0;
            while (result.next()) {
                actorid = result.getInt("actorid");
            }
            statement = connection.prepareStatement("select count(*) as total from affiliation where actorid = ? and organisationname = ? and role = ? and startdate = ? and enddate = ?");
            statement.setInt(1,actorid);
            statement.setString(2,string3);
            statement.setString(3,string4);
            statement.setString(4,string5);
            statement.setString(5,string6);
            result = statement.executeQuery();
            int count = 0;
            while (result.next()){
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Given("All organisations are deleted so there are no organisations in the database")
    public void allOrganisationsAreDeletedSoThereAreNoOrganisationsInTheDatabase() {
        try {
            PreparedStatement statement = connection.prepareStatement("delete from organisation");
            statement.executeUpdate();
            statement = connection.prepareStatement("select count(*) as total from organisation");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while(result.next()){
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I create an affiliation using the file {string}")
    public void iCreateAnAffiliationUsingTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The affiliation is not stored into the database")
    public void theAffiliationIsNotStoredIntoTheDatabase() {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from affiliation");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while(result.next()){
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("The resulting command line log has an error message that there are no organisations and matches the file {string}")
    public void theResultingCommandLineLogHasAnErrorMessageThatThereAreNoOrganisationsAndMatchesTheFile(String string) {
        testDatabaseManager.compareStringToFile(outContent.toString(), string);
    }

    @Given("All actors are deleted so there are no actors in the database")
    public void allActorsAreDeletedSoThereAreNoActorsInTheDatabase() {
        try {
            PreparedStatement statement = connection.prepareStatement("delete from actor");
            statement.executeUpdate();
            statement = connection.prepareStatement("select count(*) as total from actor");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while(result.next()){
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I create an affiliation without an actor using the file {string}")
    public void iCreateAnAffiliationWithoutAnActorUsingTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The affiliation is not stored into the database because there is no actors")
    public void theAffiliationIsNotStoredIntoTheDatabaseBecauseThereIsNoActors() {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from affiliation");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while(result.next()){
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("The resulting command line log has an error message that there are no actors and matches the file {string}")
    public void theResultingCommandLineLogHasAnErrorMessageThatThereAreNoActorsAndMatchesTheFile(String string) {
        testDatabaseManager.compareStringToFile(outContent.toString(), string);
    }

    @Given("The actor with first name {string} and last name {string} already has an affiliation to the organisation {string} with role {string} and start date {string} and end date {string}")
    public void theActorWithFirstNameAndLastNameAlreadyHasAnAffiliationToTheOrganisationWithRoleAndStartDateAndEndDate(String string, String string2, String string3, String string4, String string5, String string6) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into organisation(name) values (?)");
            preparedStatement.setString(1,string3);
            preparedStatement.closeOnCompletion();
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("select actorid from actor where firstname = ? and lastname = ?");
            preparedStatement.setString(1,string);
            preparedStatement.setString(2,string2);
            ResultSet result = preparedStatement.executeQuery();
            Integer actorid = null;
            while(result.next()){
                actorid = result.getInt("actorid");
            }
            preparedStatement = connection.prepareStatement(
                    "insert into affiliation(actorid,organisationname,role,startdate,enddate) values (?,?,?,?,?)");
            preparedStatement.setInt(1,actorid);
            preparedStatement.setString(2,string3);
            preparedStatement.setString(3,string4);
            preparedStatement.setString(4,string5);
            preparedStatement.setString(5,string6);
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            Assert.assertTrue(count == 1);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @When("I create another affiliation with the actor of first name Izuku and last name Midoriya with the organisation UA with role Hero and start date February eighteenth twenty seventeen and end date December twentieth twenty twenty using the file {string}")
    public void iCreateAnotherAffiliationWithTheActorOfFirstNameIzukuAndLastNameMidoriyaWithTheOrganisationUAWithRoleHeroAndStartDateFebruaryEighteenthTwentySeventeenAndEndDateDecemberTwentiethTwentyTwentyUsingTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The new affiliation with actor of first name {string} and last name {string} and organisation {string} with role {string} and start date {string} and end date {string} is stored into the database")
    public void theNewAffiliationWithActorOfFirstNameAndLastNameAndOrganisationWithRoleAndStartDateAndEndDateIsStoredIntoTheDatabase(String string, String string2, String string3, String string4, String string5, String string6) {
        try {
            PreparedStatement statement = connection.prepareStatement("select actorid from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            int actorid = 0;
            while (result.next()) {
                actorid = result.getInt("actorid");
            }
            statement = connection.prepareStatement("select count(*) as total from affiliation where actorid = ? and organisationname = ? and role = ? and startdate = ? and enddate = ?");
            statement.setInt(1,actorid);
            statement.setString(2,string3);
            statement.setString(3,string4);
            statement.setString(4,string5);
            statement.setString(5,string6);
            result = statement.executeQuery();
            int count = 0;
            while (result.next()){
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("The actor with first name {string} and last name {string} has two affiliations")
    public void theActorWithFirstNameAndLastNameHasTwoAffiliations(String string, String string2) {
        try {
            PreparedStatement statement = connection.prepareStatement("select actorid from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            int actorid = 0;
            while (result.next()) {
                actorid = result.getInt("actorid");
            }
            statement = connection.prepareStatement("select count(*) as total from affiliation where actorid = ?");
            statement.setInt(1,actorid);
            result = statement.executeQuery();
            int count = 0;
            while (result.next()){
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I create an affiliation with end date prior to start date using the file {string}")
    public void iCreateAnAffiliationWithEndDatePriorToStartDateUsingTheFile(String string) {
        try {
            runApplicationWithFile(string);
        }
        catch(NoSuchElementException e){
        }
    }

    @Then("The affiliation is not stored into the database because the end date is prior to start date")
    public void theAffiliationIsNotStoredIntoTheDatabaseBecauseTheEndDateIsPriorToStartDate() {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from affiliation");
            ResultSet result = statement.executeQuery();
            int count = -1;
            while(result.next()){
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("The resulting command line log has an error message that the end date is prior to start date and matches the file {string}")
    public void theResultingCommandLineLogHasAnErrorMessageThatTheEndDateIsPriorToStartDateAndMatchesTheFile(String string) {
        testDatabaseManager.compareStringToFile(outContent.toString(), string);
    }

    public void runApplicationWithFile(String filePath) throws NoSuchElementException{
        ApplicationManager application = new ApplicationManager();
        String[] args = {"test"};
        final InputStream original = System.in;
        final FileInputStream fips;
        try {
            fips = new FileInputStream(new File("./src/test/resources/inputfiles/" + filePath));
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));
            System.setIn(fips);
            application.main(args);
            System.setIn(original);
            System.setOut(originalOut);
            System.setErr(originalErr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @When("I create an affiliation with the actor of first name Izuku and last name Midoriya with the organisation UA with no role with no start date with no end date using the file {string}")
    public void iCreateAnAffiliationWithTheActorOfFirstNameIzukuAndLastNameMidoriyaWithTheOrganisationUAWithNoRoleWithNoStartDateWithNoEndDateUsingTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The affiliation with actor of first name {string} and last name {string} and organisation {string} with no role and no start date and no end date is stored into the database")
    public void theAffiliationWithActorOfFirstNameAndLastNameAndOrganisationWithNoRoleAndNoStartDateAndNoEndDateIsStoredIntoTheDatabase(String string, String string2, String string3) {
        try {
            PreparedStatement statement = connection.prepareStatement("select actorid from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            statement.closeOnCompletion();
            ResultSet result = statement.executeQuery();
            int actorid = 0;
            while (result.next()) {
                actorid = result.getInt("actorid");
            }
            statement = connection.prepareStatement("select * from affiliation where actorid = ? and organisationname = ?");
            statement.setInt(1,actorid);
            statement.setString(2,string3);
            statement.closeOnCompletion();
            result = statement.executeQuery();
            String role = "temp";
            String startDate = "temp";
            String endDate = "temp";
            while(result.next()){
                role = result.getString("role");
                startDate = result.getString("startdate");
                endDate = result.getString("enddate");
            }
            Assert.assertNull(role);
            Assert.assertNull(startDate);
            Assert.assertNull(endDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
