# Created by mzh73 at 17/04/19


Feature: Organisation functionality


  Background: Connect to database
    Given I am connected to the discourse database

  Scenario: Insert an organisation into organisation table with an not existed organisation name
    Given The organisation with name "university of auckland" dose not exists
    When I insert organisation with existed name "university of auckland" to organisation
    Then I got a confirmation message with "Success" as I insert successfully.

  Scenario: Insert an organisation into organisation table with an existed organisation name
    Given The organisation with name "University of canterbury" exists
    When I insert organisation with not existed name "University of canterbury" to organisation
    Then I got a confirmation message with "Fail" as I insert fail.



