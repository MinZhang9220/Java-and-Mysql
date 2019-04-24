# Created by minzhang at 2019-04-20
Feature: Argument functionality


  Background: Connected to the database
    Given I am connected to the test database

  Scenario: Creating a valid argument
    Given  There are no identical arguments in the database with discourse id 5 and start index 0 and end index 133
    When I rephrase the argument sentence extracted from discourse id five and defined by start index zero and end index one hundred and thirty three to the rephrasing Kare wa nandemo dekiru using the file "CreateAValidArgument"
    Then The argument of discourse id 5 and start index 0 and end index 133 and rephrasing "Kare wa nandemo dekiru" is stored into the database
    And The resulting command line log from creating the argument has a success confirmation message and matches the file "CreatingAValidArgument"

  Scenario: Creating an identical argument
    Given  There is an existing argument in the database with discourse id 5 and start index 0 and end index 133 and rephrasing "Filler rephrasing"
    When I rephrase the identical argument sentence extracted from discourse id five and defined by start index zero and end index one hundred and thirty three to the rephrasing Kare wa nandemo dekiru using the file "CreateAnIdenticalArgument"
    Then There is still only one argument of discourse id 5 and start index 0 and end index 133 in the database
    And The resulting command line log has an identical argument error message message and matches the file "CreatingAnIdenticalArgument"

  Scenario: Creating an identical argument with the same sentence but with whitespace after the sentence
    Given  There is an argument in the database with discourse id 5 and start index 0 and end index 133 and rephrasing "Filler rephrasing"
    When I rephrase the identical argument sentence extracted from discourse id five and defined by start index zero and end index one hundred and thirty six to the rephrasing Kare wa nandemo dekiru using the file "CreateAnIdenticalArgumentWithWhitespace"
    Then There is only one argument of discourse id 5 and start index 0 and end index 133 in the database
    And There is no argument with discourse id 5 and start index 0 and end index 136 in the database
    And The resulting command line log has an identical argument error message and it matches the file "CreatingAnIdenticalArgumentWithWhitespace"

  Scenario: Creating an argument from a sentence starting after a question mark and ending with an exclamation mark
    Given  There exists no identical arguments in the database with discourse id 4 and start index 71 and end index 112
    When I rephrase the argument sentence extracted from discourse id four and defined by start index seventy one and end index one hundred and twelve to the rephrasing Plus Ultra! using the file "CreateAValidArgumentWithoutFullStops"
    Then The argument of discourse id 4 and start index 71 and end index 112 and rephrasing "Plus ultra!" is successfully stored into the database
    And The resulting command line log from creating the argument contains a success confirmation message and matches the file "CreatingAValidArgumentWithoutFullStops"

  #Scenario: Insert an argument into database
   # Given  The argument which be rephrased from discourse 1 with start indices is 1 and end indices is 10 dose not exist in database
    #When I rephrased an argument from the discourse with it's id is 1,with start indices is 1,end indices is 10 for insert into database
    #Then I got a confirmation message with "Success" as I insert argument successfully.

  #Scenario: Can not insert an argument into database as the argument already exists in database
   # Given  The argument which be rephrased from discourse 2 with start indices is 0 and end indices is 1  which exist in database
    #When I rephrased an argument from the discourse with it's id is 2,with start indices is 0,end indices is 1 which argument exist in database
    #Then I got a confirmation message with "Fail" as I insert argument Fail.

  #Scenario: Non valid start indices is less than the lower boundary
  #  When I rephrased an argument from the discourse with it's id is 2,with start indices is -1,end indices is 10 for check start indices
   # Then I got a confirmation message with "Start indices is wrong" as start indices is wrong.

  #Scenario: Non valid end indices is greater than the biggest boundary
   # When I rephrased an argument from the discourse with it's id is 2,with start indices is 0,end indices is 200 for check end indices
    #Then I got a confirmation message with "End indices is wrong" as end indices is wrong.

  #Scenario: Non valid end indices start indices is greater than the end indices
   # When I rephrased an argument from the discourse with it's id is 2,with start indices is 4,end indices is 2 for check start and end indices
    #Then I got a confirmation message with "Indices is wrong" as start indices is less than the end indices.