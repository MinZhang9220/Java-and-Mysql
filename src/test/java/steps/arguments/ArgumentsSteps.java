package steps.arguments;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import function.Argument;
import function.SQLiteJDBC;
import org.junit.Assert;

import java.sql.Connection;

public class ArgumentsSteps {


    Argument argument = new Argument();
    String[] confirm_creat_argument = new String[2];
    String[] valid_indices_result = new String[2];
    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Connection connection;

    @Given("I am connected to the discourse database")
    public void iAmConnectedToTheDiscourseDatabase(){
        connection = sqLiteJDBC.getConnectionToDatabase();
        Assert.assertFalse(connection == null);
    }


    @Given("The argument which be rephrased from discourse {int} with start indices is {int} and end indices is {int} dose not exist in database")
    public void checkArgumentNotExistInDatabase(int discourseId,int startIndices,int endIndices){
        boolean result = argument.validExistInDatabase(discourseId,startIndices,endIndices,connection);
        Assert.assertFalse(result);
    }

    @When("I rephrased an argument from the discourse with it's id is {int},with start indices is {int},end indices is {int} for insert into database")
    public void iInsertAnArgumentIntoDatabase(int discourseId,int startIndices,int endIndices){
        confirm_creat_argument = argument.createArgument(discourseId,startIndices,endIndices,connection);
        Assert.assertEquals("true",confirm_creat_argument[0]);
    }

    @Then("I got a confirmation message with {string} as I insert argument successfully.")
    public void iGotAMessageForCreateArgument(String message){
        Assert.assertEquals(message,confirm_creat_argument[1]);
    }


    @Given("The argument which be rephrased from discourse {int} with start indices is {int} and end indices is {int}  which exist in database")
    public void theArgumentWhichBeRephrasedFromDiscourseWithStartIndicesIsAndEndIndicesIsWhichExistInDatabase(Integer int1, Integer int2, Integer int3) {
        // Write code here that turns the phrase above into concrete actions
        boolean result = argument.validExistInDatabase(int1,int2,int3,connection);
        Assert.assertTrue(result);
    }

    @When("I rephrased an argument from the discourse with it's id is {int},with start indices is {int},end indices is {int} which argument exist in database")
    public void iInsertAnExistedArgumentIntoDatabase(int discourseId,int startIndices,int endIndices){
        confirm_creat_argument = argument.createArgument(discourseId,startIndices,endIndices,connection);
        Assert.assertEquals("false",confirm_creat_argument[0]);
    }

    @Then("I got a confirmation message with {string} as I insert argument Fail.")
    public void iGotAMessageForCanNotCreateArgument(String message){
        Assert.assertEquals(message,confirm_creat_argument[1]);
    }


    @When("I rephrased an argument from the discourse with it's id is {int},with start indices is {int},end indices is {int} for check start indices")
    public void checkStartIndicesIsNegativeInteger(int discourseId,int startIndices,int endIndieces){
        valid_indices_result = argument.validIndex(discourseId,startIndices,endIndieces);
        Assert.assertEquals("false",valid_indices_result[0]);
    }



    @Then("I got a confirmation message with {string} as start indices is wrong.")
    public void iGotAConfirmationMessageWithAsStartIndicesIsWrong(String string) {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertEquals(string,valid_indices_result[1]);
    }

    @When("I rephrased an argument from the discourse with it's id is {int},with start indices is {int},end indices is {int} for check end indices")
    public void checkEndIndicesIsGreaterThanUpperBoundaries(int discourseId,int startIndices,int endIndieces){
        valid_indices_result = argument.validIndex(discourseId,startIndices,endIndieces);
        Assert.assertEquals("false",valid_indices_result[0]);
    }


    @Then("I got a confirmation message with {string} as end indices is wrong.")
    public void iGotAConfirmationMessageWithAsEndIndicesIsWrong(String string) {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertEquals(string,valid_indices_result[1]);
    }

    @When("I rephrased an argument from the discourse with it's id is {int},with start indices is {int},end indices is {int} for check start and end indices")
    public void checkStartIndicesIsGreaterThanEndIndices(int discourseId,int startIndices,int endIndieces){
        valid_indices_result = argument.validIndex(discourseId,startIndices,endIndieces);
        Assert.assertEquals("false",valid_indices_result[0]);
    }

    @Then("I got a confirmation message with {string} as start indices is less than the end indices.")
    public void iGotAConfirmationMessageWithAsStartIndicesIsLessThanTheEndIndices(String string) {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertEquals(string,valid_indices_result[1]);

    }
}
