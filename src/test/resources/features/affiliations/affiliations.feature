# Created by minzhang at 2019-04-20
Feature: Affiliation functionality

  Background: Connected to the database
    Given I am connected to the test database
    And There exists an actor with first name "Izuku" and last name "Midoriya" in the database
    And There exists an organisation with the name "U.A" in the database

  Scenario: Creating a valid affiliation with a role and start date and end date
    When I create an affiliation with the actor of first name Izuku and last name Midoriya with the organisation UA with role Hero and start date February eighteenth twenty seventeen and end date December twentieth twenty twenty using the file "CreateValidAffiliationWithRoleStartDateEndDate"
    Then The affiliation with actor of first name "Izuku" and last name "Midoriya" and organisation "U.A" with role "Hero" and start date "2017-02-18" and end date "2020-12-20" is stored into the database

  Scenario: Creating an affiliation without an organisation in the database
    Given All organisations are deleted so there are no organisations in the database
    When I create an affiliation using the file "CreateAffiliationWithNoOrganisationInTheDatabase"
    Then The affiliation is not stored into the database
    And The resulting command line log has an error message that there are no organisations and matches the file "CreatingAnAffiliationWithoutAnOrganisationInTheDatabase"

  Scenario: Creating an affiliation without an actor in the database
    Given All actors are deleted so there are no actors in the database
    When I create an affiliation without an actor using the file "CreateAffiliationWithNoActorInTheDatabase"
    Then The affiliation is not stored into the database because there is no actors
    And The resulting command line log has an error message that there are no actors and matches the file "CreatingAnAffiliationWithoutAnActorInTheDatabase"

  Scenario: Creating multiple affiliations for the same actor
    Given The actor with first name "Izuku" and last name "Midoriya" already has an affiliation to the organisation "University of Canterbury" with role "Student" and start date "2017-02-18" and end date "2020-12-20"
    When I create another affiliation with the actor of first name Izuku and last name Midoriya with the organisation UA with role Hero and start date February eighteenth twenty seventeen and end date December twentieth twenty twenty using the file "CreateAnotherAffiliationWithRoleStartDateEndDate"
    Then The new affiliation with actor of first name "Izuku" and last name "Midoriya" and organisation "U.A" with role "Hero" and start date "2017-02-18" and end date "2020-12-20" is stored into the database
    And The actor with first name "Izuku" and last name "Midoriya" has two affiliations

  Scenario: Creating an affiliation with start date prior to end date
    When I create an affiliation with end date prior to start date using the file "CreateAffiliationWithEndDatePriorToStartDate"
    Then The affiliation is not stored into the database because the end date is prior to start date
    And The resulting command line log has an error message that the end date is prior to start date and matches the file "CreatingAnAffiliationWithStartDatePriorToEndDate"

  Scenario: Creating an affiliation with optional fields not filled in
    When I create an affiliation with the actor of first name Izuku and last name Midoriya with the organisation UA with no role with no start date with no end date using the file "CreateAffiliationWithNoRoleNoStartDateNoEndDate"
    Then The affiliation with actor of first name "Izuku" and last name "Midoriya" and organisation "U.A" with no role and no start date and no end date is stored into the database