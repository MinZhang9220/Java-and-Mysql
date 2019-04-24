# Created by minzhang at 2019-04-20
Feature: Actor functionality

  Background: Connected to the database
    Given I am connected to the test database

  Scenario: Validly creating a non homonym actor
    Given  There is no actor with first name "Izuku" and last name "Midoriya" in the database
    When I insert an actor called Izuku Midoriya with a level of trust of one using input from the file "CreateANonHomonymActor"
    Then The actor with first name "Izuku" and last name "Midoriya" is stored into the database
    And the resulting command line log from creating an actor has a confirmation message and matches the file "ValidlyCreatingANonHomonymActor"

  Scenario: Creating a non homonym actor and denying confirmation
    Given  There is one actor with first name "Izuku" and last name "Midoriya" in the database
    And The actor with first name "Izuku" and last name "Midoriya" has an affiliation to the organisation "University of Canterbury" with role "Hero" and start date "2017-02-18" and end date "2020-12-20"
    When I insert an actor called Izuku Midoriya with a level of trust of one and deny the confirmation using input from the file "CreateAHomonymActorAndDenyConfirmation"
    Then The actor with first name "Izuku" and last name "Midoriya" is not stored into the database
    And The resulting command line log from unsuccessfully creating an actor has a confirmation message and matches the file "CreatingANonHomonymActorAndDenyingConfirmation"

  Scenario: Creating a non homonym actor and accepting confirmation
    Given  There is an actor with first name "Izuku" and last name "Midoriya" in the database
    And The actor with first name "Izuku" and last name "Midoriya" has one affiliation to the organisation "University of Canterbury" with role "Hero" and start date "2017-02-18" and end date "2020-12-20"
    When I insert an actor called Izuku Midoriya with a level of trust of one and accept the confirmation using input from the file "CreateAHomonymActorAndAcceptConfirmation"
    Then The actor with first name "Izuku" and last name "Midoriya" is successfully stored into the database
    And The resulting command line log from successfully creating an actor has a confirmation message and matches the file "CreatingANonHomonymActorAndAcceptingConfirmation"

  Scenario: Creating an actor with empty first name and last name
    Given  There are no actors with an empty first name "" and empty last name "" in the database
    When I create an actor with an empty first name and empty last name and level of trust of one using the file "CreateAnActorWithEmptyFirstAndLastName"
    Then The actor without a name of first name "" and last name "" is not stored into the database
    And The resulting command line log from unsuccessfully creating an actor with no name has an error message and matches the file "CreatingAnActorWithEmptyFirstNameAndLastName"

  Scenario: Creating an actor with first name and empty last name
    Given  There are no actors with first name "Peter" and empty last name "" in the database
    When I create an actor with first name Peter and empty last name and level of trust of one using the file "CreateAnActorWithFirstNameAndEmptyLastName"
    Then The actor of first name "Peter" and last name "" is not stored into the database
    And The resulting command line log from unsuccessfully creating an actor with an empty last name has an error message and matches the file "CreatingAnActorWithFirstNameAndEmptyLastName"

  Scenario: Creating an actor with invalid level of trust
    Given  There are no actors with first name "Peter" and last name "Parker" in the database
    When I create an actor with first name Peter and last name Parker but level of trust of less than zero then more than one then one using the file "CreateAnActorWithInvalidLevelOfTrust"
    Then The actor of first name "Peter" and last name "Parker" is stored into the database with level of trust of one
    And The resulting command line log has two error messages for invalid level of trust and matches the file "CreatingAnActorWithInvalidLevelOfTrust"
