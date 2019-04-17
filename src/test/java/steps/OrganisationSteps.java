package steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.And;
import cucumber.api.java.en.But;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import java.sql.Connection;
import java.sql.SQLException;
import function.SQLiteJDBC;
import function.Organisation;
import org.junit.Assert;





public class OrganisationSteps {

    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    Organisation organisation = new Organisation();
    Connection connection;
    String[] confirm_insert = new String[2];


    @Given("I am connected to the discourse database")
    public void iAmConnectedToTheDiscourseDatabase(){
        connection = sqLiteJDBC.connectionToDatabase();
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



}
