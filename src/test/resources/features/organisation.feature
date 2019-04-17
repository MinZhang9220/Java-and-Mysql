# Created by mzh73 at 17/04/19


Feature: Organisation functionality


  Background: Given I am connected to the discourse database

  Scenario: Insert an organisation into organisation table with an not existed organisation name
    Given The organisation with name "university of auckland" dose not exists
    When I insert organisation with name "university of auckland" to organisation
    Then I got a confirmation message with "Success"

  Scenario: Insert an organisation into organisation table wiht an existed organisation name
    Given The organisation with name "university of auckland" dose not exists
    When I insert organisation with name "university of auckland" to organisation
    Then I got a confirmation message with "Success"





    # Enter steps here