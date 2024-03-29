package steps;

import controllers.ApplicationManager;
import cucumber.api.java.*;
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

public class ActorTestSteps {

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


    @Given("There is no actor with first name {string} and last name {string} in the database")
    public void thereIsNoActorWithFirstNameAndLastNameInTheDatabase(String string, String string2) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            Assert.assertFalse(count > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I insert an actor called Izuku Midoriya with a level of trust of one using input from the file {string}")
    public void iInsertAnActorCalledIzukuMidoriyaWithALevelOfTrustOfOneUsingInputFromTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The actor with first name {string} and last name {string} is stored into the database")
    public void theActorWithFirstNameAndLastNameIsStoredIntoTheDatabase(String string, String string2) {
        try {
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

    @Then("the resulting command line log from creating an actor has a confirmation message and matches the file {string}")
    public void theResultingCommandLineLogFromCreatingAnActorHasAConfirmationMessageAndMatchesTheFile(String string) {
        testDatabaseManager.compareStringToFile(outContent.toString(), string);
    }

    @Given("There is one actor with first name {string} and last name {string} in the database")
    public void thereIsOneActorWithFirstNameAndLastNameInTheDatabase(String string, String string2) {
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

    @Given("The actor with first name {string} and last name {string} has an affiliation to the organisation {string} with role {string} and start date {string} and end date {string}")
    public void theActorWithFirstNameAndLastNameHasAnAffiliationToTheOrganisationWithRoleAndStartDateAndEndDate(String string, String string2, String string3, String string4, String string5, String string6) {
        // Write code here that turns the phrase above into concrete actions
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
            Assert.assertTrue(count > 0);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @When("I insert an actor called Izuku Midoriya with a level of trust of one and deny the confirmation using input from the file {string}")
    public void iInsertAnActorCalledIzukuMidoriyaWithALevelOfTrustOfOneAndDenyTheConfirmationUsingInputFromTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The actor with first name {string} and last name {string} is not stored into the database")
    public void theActorWithFirstNameAndLastNameIsNotStoredIntoTheDatabase(String string, String string2) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            Assert.assertFalse(count != 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("The resulting command line log from unsuccessfully creating an actor has a confirmation message and matches the file {string}")
    public void theResultingCommandLineLogFromUnsuccessfullyCreatingAnActorHasAConfirmationMessageAndMatchesTheFile(String string) {
        testDatabaseManager.compareStringToFile(outContent.toString(), string);
    }

    @Given("There is an actor with first name {string} and last name {string} in the database")
    public void thereIsAnActorWithFirstNameAndLastNameInTheDatabase(String string, String string2) {
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

    @Given("The actor with first name {string} and last name {string} has one affiliation to the organisation {string} with role {string} and start date {string} and end date {string}")
    public void theActorWithFirstNameAndLastNameHasOneAffiliationToTheOrganisationWithRoleAndStartDateAndEndDate(String string, String string2, String string3, String string4, String string5, String string6) {
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

    @When("I insert an actor called Izuku Midoriya with a level of trust of one and accept the confirmation using input from the file {string}")
    public void iInsertAnActorCalledIzukuMidoriyaWithALevelOfTrustOfOneAndAcceptTheConfirmationUsingInputFromTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The actor with first name {string} and last name {string} is successfully stored into the database")
    public void theActorWithFirstNameAndLastNameIsSuccessfullyStoredIntoTheDatabase(String string, String string2) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("The resulting command line log from successfully creating an actor has a confirmation message and matches the file {string}")
    public void theResultingCommandLineLogFromSuccessfullyCreatingAnActorHasAConfirmationMessageAndMatchesTheFile(String string) {
        testDatabaseManager.compareStringToFile(outContent.toString(), string);
    }

    @Given("There are no actors with an empty first name {string} and empty last name {string} in the database")
    public void thereAreNoActorsWithAnEmptyFirstNameAndEmptyLastNameInTheDatabase(String string, String string2) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I create an actor with an empty first name and empty last name and level of trust of one using the file {string}")
    public void iCreateAnActorWithAnEmptyFirstNameAndEmptyLastNameAndLevelOfTrustOfOneUsingTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The actor without a name of first name {string} and last name {string} is not stored into the database")
    public void theActorWithoutANameOfFirstNameAndLastNameIsNotStoredIntoTheDatabase(String string, String string2) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("The resulting command line log from unsuccessfully creating an actor with no name has an error message and matches the file {string}")
    public void theResultingCommandLineLogFromUnsuccessfullyCreatingAnActorWithNoNameHasAnErrorMessageAndMatchesTheFile(String string) {
        testDatabaseManager.compareStringToFile(outContent.toString(), string);
    }

    @Given("There are no actors with first name {string} and empty last name {string} in the database")
    public void thereAreNoActorsWithFirstNameAndEmptyLastNameInTheDatabase(String string, String string2) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I create an actor with first name Peter and empty last name and level of trust of one using the file {string}")
    public void iCreateAnActorWithFirstNamePeterAndEmptyLastNameAndLevelOfTrustOfOneUsingTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The actor of first name {string} and last name {string} is not stored into the database")
    public void theActorOfFirstNameAndLastNameIsNotStoredIntoTheDatabase(String string, String string2) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("The resulting command line log from unsuccessfully creating an actor with an empty last name has an error message and matches the file {string}")
    public void theResultingCommandLineLogFromUnsuccessfullyCreatingAnActorWithAnEmptyLastNameHasAnErrorMessageAndMatchesTheFile(String string) {
        testDatabaseManager.compareStringToFile(outContent.toString(), string);
    }

    @Given("There are no actors with first name {string} and last name {string} in the database")
    public void thereAreNoActorsWithFirstNameAndLastNameInTheDatabase(String string, String string2) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            Assert.assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I create an actor with first name Peter and last name Parker but level of trust of less than zero then more than one then one using the file {string}")
    public void iCreateAnActorWithFirstNamePeterAndLastNameParkerButLevelOfTrustOfLessThanZeroThenMoreThanOneThenOneUsingTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The actor of first name {string} and last name {string} is stored into the database with level of trust of one")
    public void theActorOfFirstNameAndLastNameIsStoredIntoTheDatabaseWithLevelOfTrustOfOne(String string, String string2) {
        try {
            PreparedStatement statement = connection.prepareStatement("select leveloftrust from actor where firstname = ? and lastname = ?");
            statement.setString(1, string);
            statement.setString(2, string2);
            ResultSet result = statement.executeQuery();
            Double levelOfTrust = 0.0;
            while (result.next()) {
                levelOfTrust = result.getDouble("levelOfTrust");
            }
            Assert.assertTrue(levelOfTrust == 1.0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("The resulting command line log has two error messages for invalid level of trust and matches the file {string}")
    public void theResultingCommandLineLogHasTwoErrorMessagesForInvalidLevelOfTrustAndMatchesTheFile(String string) {
        testDatabaseManager.compareStringToFile(outContent.toString(), string);
    }

    public void runApplicationWithFile(String filePath){
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
}
