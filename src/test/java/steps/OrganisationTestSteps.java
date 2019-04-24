package steps;

import controllers.ApplicationManager;
import cucumber.api.java.*;
import cucumber.api.java.en.*;
import datahandling.SQLiteJDBC;
import org.junit.Assert;

import org.apache.commons.io.IOUtils;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrganisationTestSteps {

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


    @Given("I am connected to the test database")
    public void iAmConnectedToTheTestDatabase() {
        Assert.assertNotNull(connection);
    }

    @Given("The organisation with name {string} does not exist")
    public void theOrganisationWithNameDoesNotExist(String string) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from organisation where name = ?");
            statement.setString(1, string);
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


    @When("I create an organisation with the unique name University of Canterbury using input from the file {string}")
    public void iCreateAnOrganisationWithTheUniqueNameUniversityOfCanterburyUsingInputFromTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The organisation with name {string} is stored in the database")
    public void theOrganisationWithNameIsStoredInTheDatabase(String string) {
        // Write code here that turns the phrase above into concrete actions
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("select count(*) as total from organisation where name = ?");
            statement.setString(1, string);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            Assert.assertTrue(count > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("The resulting command line log has a success confirmation message and matches the file {string}")
    public void theResultingCommandLineLogHasASuccessConfirmationMessageAndMatchesTheFile(String string) {
        testDatabaseManager.compareStringToFile(outContent.toString(), string);
    }

    @Given("The organisation with name {string} already exists in the database")
    public void theOrganisationWithNameAlreadyExistsInTheDatabase(String string) {
        // Write code here that turns the phrase above into concrete actions
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

    @When("I create an organisation with the non unique name University of Canterbury using input from the file {string}")
    public void iCreateAnOrganisationWithTheNonUniqueNameUniversityOfCanterburyUsingInputFromTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("There will still only be one organisation with name {string} in the database")
    public void thereWillStillOnlyBeOneOrganisationWithNameInTheDatabase(String string) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from organisation where name = ?");
            statement.setString(1, string);
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

    @Then("The resulting command line log has an error message and matches the file {string}")
    public void theResultingCommandLineLogHasAnErrorMessageAndMatchesTheFile(String string) {
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
