# Created by mzh73 at 17/04/19


Feature: Organisation functionality


  Background: Given I am connected to the discourse database

  Scenario: Insert an organisation into organisation table
    Given The organisation with name "university of auckland" dose not exists

    # Enter steps here