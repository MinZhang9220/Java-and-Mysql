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
import java.util.NoSuchElementException;

public class ArgumentTestSteps {

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

    @Given("There are no identical arguments in the database with discourse id {int} and start index {int} and end index {int}")
    public void thereAreNoIdenticalArgumentsInTheDatabaseWithDiscourseIdAndStartIndexAndEndIndex(Integer int1, Integer int2, Integer int3) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where discourseid = ? and startindex = ? and endindex = ?");
            statement.setInt(1, int1);
            statement.setInt(2, int2);
            statement.setInt(3, int3);
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

    @When("I rephrase the argument sentence extracted from discourse id five and defined by start index zero and end index one hundred and thirty three to the rephrasing Kare wa nandemo dekiru using the file {string}")
    public void iRephraseTheArgumentSentenceExtractedFromDiscourseIdFiveAndDefinedByStartIndexZeroAndEndIndexOneHundredAndThirtyThreeToTheRephrasingKareWaNandemoDekiruUsingTheFile(String string) {
        runApplicationWithFile(string);
    }

    @Then("The argument of discourse id {int} and start index {int} and end index {int} and rephrasing {string} is stored into the database")
    public void theArgumentOfDiscourseIdAndStartIndexAndEndIndexAndRephrasingIsStoredIntoTheDatabase(Integer int1, Integer int2, Integer int3, String string) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where discourseid = ? and startindex = ? and endindex = ? and rephrasing = ?");
            statement.setInt(1, int1);
            statement.setInt(2, int2);
            statement.setInt(3, int3);
            statement.setString(4, string);
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

    @Then("The resulting command line log from creating the argument has a success confirmation message and matches the file {string}")
    public void theResultingCommandLineLogFromCreatingTheArgumentHasASuccessConfirmationMessageAndMatchesTheFile(String string) {
        testDatabaseManager.compareStringToFile(outContent.toString(), string);
    }

    @Given("There is an existing argument in the database with discourse id {int} and start index {int} and end index {int} and rephrasing {string}")
    public void thereIsAnExistingArgumentInTheDatabaseWithDiscourseIdAndStartIndexAndEndIndexAndRephrasing(Integer int1, Integer int2, Integer int3, String string) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into argument(discourseid,startindex,endindex,rephrasing) values (?,?,?,?)");
            preparedStatement.setInt(1,int1);
            preparedStatement.setInt(2,int2);
            preparedStatement.setInt(3,int3);
            preparedStatement.setString(4,string);
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            Assert.assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I rephrase the identical argument sentence extracted from discourse id five and defined by start index zero and end index one hundred and thirty three to the rephrasing Kare wa nandemo dekiru using the file {string}")
    public void iRephraseTheIdenticalArgumentSentenceExtractedFromDiscourseIdFiveAndDefinedByStartIndexZeroAndEndIndexOneHundredAndThirtyThreeToTheRephrasingKareWaNandemoDekiruUsingTheFile(String string) {
        try {
            runApplicationWithFile(string);
        }
        catch(NoSuchElementException e){
            //do nothing, because this is an expected outcome
        }
    }

    @Then("There is still only one argument of discourse id {int} and start index {int} and end index {int} in the database")
    public void thereIsStillOnlyOneArgumentOfDiscourseIdAndStartIndexAndEndIndexInTheDatabase(Integer int1, Integer int2, Integer int3) {
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as total from argument where discourseid = ? and startindex = ? and endindex = ?");
            statement.setInt(1, int1);
            statement.setInt(2, int2);
            statement.setInt(2, int3);
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

    @Then("The resulting command line log has an identical argument error message message and matches the file {string}")
    public void theResultingCommandLineLogHasAnIdenticalArgumentErrorMessageMessageAndMatchesTheFile(String string) {
            testDatabaseManager.compareStringToFile(outContent.toString(), string);
    }

    @Given("There is an argument in the database with discourse id {int} and start index {int} and end index {int} and rephrasing {string}")
    public void thereIsAnArgumentInTheDatabaseWithDiscourseIdAndStartIndexAndEndIndexAndRephrasing(Integer int1, Integer int2, Integer int3, String string) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into argument(discourseid,startindex,endindex,rephrasing) values (?,?,?,?)");
            preparedStatement.setInt(1,int1);
            preparedStatement.setInt(2,int2);
            preparedStatement.setInt(3,int3);
            preparedStatement.setString(4,string);
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            Assert.assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @When("I rephrase the identical argument sentence extracted from discourse id five and defined by start index zero and end index one hundred and thirty six to the rephrasing Kare wa nandemo dekiru using the file {string}")
    public void iRephraseTheIdenticalArgumentSentenceExtractedFromDiscourseIdFiveAndDefinedByStartIndexZeroAndEndIndexOneHundredAndThirtySixToTheRephrasingKareWaNandemoDekiruUsingTheFile(String string) {
        try {
            runApplicationWithFile(string);
        }
        catch(NoSuchElementException e){
            //do nothing, because this is an expected outcome
        }
    }

    @Then("There is only one argument of discourse id {int} and start index {int} and end index {int} in the database")
    public void thereIsOnlyOneArgumentOfDiscourseIdAndStartIndexAndEndIndexInTheDatabase(Integer int1, Integer int2, Integer int3) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into argument(discourseid,startindex,endindex,rephrasing) values (?,?,?,?)");
            preparedStatement.setInt(1,int1);
            preparedStatement.setInt(2,int2);
            preparedStatement.setInt(3,int3);
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            Assert.assertTrue(count == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("There is no argument with discourse id {int} and start index {int} and end index {int} in the database")
    public void thereIsNoArgumentWithDiscourseIdAndStartIndexAndEndIndexInTheDatabase(Integer int1, Integer int2, Integer int3) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into argument(discourseid,startindex,endindex,rephrasing) values (?,?,?,?)");
            preparedStatement.setInt(1,int1);
            preparedStatement.setInt(2,int2);
            preparedStatement.setInt(3,int3);
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            Assert.assertTrue(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("The resulting command line log has an identical argument error message and it matches the file {string}")
    public void theResultingCommandLineLogHasAnIdenticalArgumentErrorMessageAndItMatchesTheFile(String string) {
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
