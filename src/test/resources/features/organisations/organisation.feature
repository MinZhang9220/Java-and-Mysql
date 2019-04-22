# Created by mzh73 at 17/04/19


Feature: Organisation functionality


  Background: Connected to the database
    Given I am connected to the test database

  Scenario: Creating an organisation with a unique organisation name
    Given The organisation with name "University of Canterbury" does not exist
    When I create an organisation with the unique name University of Canterbury using input from the file "CreateAnOrganisationNamedUniversityOfCanterbury"
    Then The organisation with name "University of Canterbury" is stored in the database
    And The resulting command line log has a success confirmation message and matches the file "CreatingAnOrganisationWithAUniqueOrganisationName"

  Scenario: Creating an organisation with an existing name
    Given The organisation with name "University of Canterbury" already exists in the database
    When I create an organisation with the non unique name University of Canterbury using input from the file "CreateAnOrganisationNamedUniversityOfCanterbury"
    Then There will still only be one organisation with name "University of Canterbury" in the database
    And The resulting command line log has an error message and matches the file "CreatingAnOrganisationWithAnExistingName"