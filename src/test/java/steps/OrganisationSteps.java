package steps;


public class OrganisationSteps {
    /*

    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Organisation organisation = new Organisation();
    Argument argument = new Argument();
    Connection connection;
    String[] confirm_insert = new String[2];
    String[] confirm_creat_argument = new String[2];
    String[] valid_indices_result = new String[2];


    @Given("I am connected to the discourse database")
    public void iAmConnectedToTheDiscourseDatabase(){
        connection = sqLiteJDBC.getConnectionToDatabase();
        Assert.assertFalse(connection == null);
    }

    @Given("The organisation with name {string} dose not exists")
    public void checkUseNotExistedOrganisationName(String organisationName){
        boolean result = organisation.validOrganisationName(organisationName,connection);
        Assert.assertTrue(result);
    }

    @When("I insert organisation with existed name {string} to organisation")
    public void insertToOrganisationTableWithNotExistedName(String organisationName){
        confirm_insert = organisation.createOrganisation(organisationName,connection);
        Assert.assertEquals("true",confirm_insert[0]);

    }

    @Then("I got a confirmation message with {string} as I insert successfully.")
    public void getAConfirmationMessage(String message){

        Assert.assertEquals(message,confirm_insert[1]);
    }

    @Given("The organisation with name {string} exists")
    public void checkUseExistedOrganisationNameWithNotExistedName(String organisationName){
        boolean result = organisation.validOrganisationName(organisationName,connection);
        Assert.assertFalse(result);
    }

    @When("I insert organisation with not existed name {string} to organisation")
    public void insertToOrganisationTableWithExistedName(String organisationName){
        confirm_insert = organisation.createOrganisation(organisationName,connection);
        Assert.assertEquals("false",confirm_insert[0]);

    }

    @Then("I got a confirmation message with {string} as I insert fail.")
    public void iGotAConfirmationMessageWithAsIInsertFail(String string) {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertEquals(string,confirm_insert[1]);
    }

    @Given("The argument which be rephrased from discourse {int} with start indices is {int} and end indices is {int} dose not exist in database")
    public void checkArgumentNotExistInDatabase(int discourseId,int startIndices,int endIndices){
        boolean result = argument.validExistInDatabase(discourseId,startIndices,endIndices,connection);
        Assert.assertTrue(result);
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

    @Given("The argument which be rephrased from discourse {int} with start indices is {int} and end indices is {int} exist in database")
    public void checkArgumentExistInDatabase(int discourseId,int startIndices,int endIndices){
        boolean result = argument.validExistInDatabase(discourseId,startIndices,endIndices,connection);
        Assert.assertFalse(result);
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

    @Then(" I got a confirmation message with {string} as start indices is wrong.")
    public void iGotAMessageForStartIndices(String message){
        Assert.assertEquals(message,valid_indices_result[1]);
    }

    @When("I rephrased an argument from the discourse with it's id is {int},with start indices is {int},end indices is {int} for check end indices")
    public void checkEndIndicesIsGreaterThanUpperBoundaries(int discourseId,int startIndices,int endIndieces){
        valid_indices_result = argument.validIndex(discourseId,startIndices,endIndieces);
        Assert.assertEquals("false",valid_indices_result[0]);
    }

    @Then(" I got a confirmation message with {string} as end indices is wrong.")
    public void iGotAMessageForEndIndicesIsWrong(String message){
        Assert.assertEquals(message,valid_indices_result[1]);
    }

    @When("I rephrased an argument from the discourse with it's id is {int},with start indices is {int},end indices is {int} for check start and end indices")
    public void checkStartIndicesIsGreaterThanEndIndices(int discourseId,int startIndices,int endIndieces){
        valid_indices_result = argument.validIndex(discourseId,startIndices,endIndieces);
        Assert.assertEquals("false",valid_indices_result[0]);
    }

    @Then(" I got a confirmation message with {string} as start indices is less than the end indices.")
    public void iGotAMessageForStartIndicesIsGreaterThanEndIndices(String message){
        Assert.assertEquals(message,valid_indices_result[1]);
    }



*/



}
