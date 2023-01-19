Feature: Send Alert

  Background:
    * url urlSend

  Scenario: Successful case send alert with operation 0
    * def operation = 0
    Given request read("../data/alert.json")
    When method POST
    Then status 200

  Scenario: Successful case send alert with operation 1
    * def operation = 1
    Given request read("../data/alert.json")
    When method POST
    Then status 200

  Scenario: Successful case send alert with operation 2
    * def operation = 2
    Given request read("../data/alert.json")
    When method POST
    Then status 200

  Scenario: Successful case send alert with operation 3
    * def operation = 3
    Given request read("../data/alert.json")
    When method POST
    Then status 200

  Scenario: Successful case send alert with operation 5
    * def operation = 5
    Given request read("../data/alert.json")
    When method POST
    Then status 200

  Scenario: Error case send alert, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
