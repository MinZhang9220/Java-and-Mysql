# Created by minzhang at 2019-04-19

Feature: Register an argument functionality



  Background: Connect to database
    Given I am connected to the discourse database

  Scenario: Insert an argument into database
    Given  The argument which be rephrased from discourse 1 with start indices is 1 and end indices is 10 dose not exist in database
    When I rephrased an argument from the discourse with it's id is 1,with start indices is 1,end indices is 10
    Then I got a confirmation message with "Success" as I insert successfully.

  Scenario: Can not insert an argument into database as the argument already exists in database
    Given  The argument which be rephrased from discourse 2 with start indices is 1 and end indices is 10 exists in database
    When I rephrased an argument from the discourse with it's id is 2,with start indices is 1,end indices is 10
    Then I got a confirmation message with "Fail" as I insert Fail.

  Scenario: Can not insert an argument into database as the start indices is less than the lower boundary
    Given The argument which be rephrased from discourse 3 with start indices is 1 and end indices is 10 dose not exist in database
    When I rephrased an argument from the discourse with it's id is 2,with start indices is -1,end indices is 10
    Then I got a confirmation message with "Start indices is wrong" as I insert Fail.

  Scenario: Can not insert an argument into database as the end indices is greater than the biggest boundary
    Given The argument which be rephrased from discourse 3 with start indices is 1 and end indices is 10 dose not exist in database
    When I rephrased an argument from the discourse with it's id is 2,with start indices is -1,end indices is 10
    Then I got a confirmation message with "End indices is wrong" as I insert Fail.