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

  Scenario: Insert an argument into database
    Given  The argument which be rephrased from discourse 1 with start indices is 1 and end indices is 10 dose not exist in database
    When I rephrased an argument from the discourse with it's id is 1,with start indices is 1,end indices is 10 for insert into database
    Then I got a confirmation message with "Success" as I insert argument successfully.

  Scenario: Can not insert an argument into database as the argument already exists in database
    Given  The argument which be rephrased from discourse 2 with start indices is 1 and end indices is 10 exists in database which exist in database
    When I rephrased an argument from the discourse with it's id is 2,with start indices is 0,end indices is 1 which argument exist in database
    Then I got a confirmation message with "Fail" as I insert argument Fail.

  Scenario: Non valid start indices is less than the lower boundary
    When I rephrased an argument from the discourse with it's id is 2,with start indices is -1,end indices is 10 for check start indices
    Then I got a confirmation message with "Start indices is wrong" as start indices is wrong.

  Scenario: Non valid end indices is greater than the biggest boundary
    When I rephrased an argument from the discourse with it's id is 2,with start indices is 0,end indices is 200 for check end indices
    Then I got a confirmation message with "End indices is wrong" as end indices is wrong.

  Scenario: Non valid end indices start indices is greater than the end indices
    When I rephrased an argument from the discourse with it's id is 2,with start indices is 4,end indices is 2 for check start and end indices
    Then I got a confirmation message with "Indices is wrong" as start indices is less than the end indices.



