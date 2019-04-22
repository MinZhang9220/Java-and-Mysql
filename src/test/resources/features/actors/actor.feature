# Created by minzhang at 2019-04-20
Feature: Arugment functionality

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