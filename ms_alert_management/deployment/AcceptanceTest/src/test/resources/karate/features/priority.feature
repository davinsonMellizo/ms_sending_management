Feature: CRUD and relations with priority

  Background:
    * url urlPriority

  Scenario: Successful case Save priority
    * def id = 3
    * def code = 3
    Given request read("../data/priority.json")
    When method POST
    Then status 200
    And match $.id == 3

  Scenario: Successful case Find priority by id
    * def urlFind = urlPriority + "/3"
    Given url urlFind
    When method GET
    Then status 200
    And match $.id == 3

  Scenario: Successful case Update priority
    * def id = 3
    * def code = 4
    Given request read("../data/priority.json")
    When method PUT
    Then status 200
    And match $.actual.id == 3

  Scenario: Successful case Delete priority by id
    * def urlDelete = urlPriority + "/3"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == '3'

  Scenario: Error case Find priority by id
    * def urlFind = urlPriority + "/8"
    Given url urlFind
    When method GET
    Then status 500
    And match $.code == '382'

  Scenario: Error case Update priority
    * def id = "ANF"
    Given request read("../data/priority.json")
    When method PUT
    Then status 500
    And match $.code == '302'

  Scenario: Error case Delete priority by id
    * def urlDelete = urlPriority + "/8"
    Given url urlDelete
    When method DELETE
    Then status 500
    And match $.code == '382'

  Scenario: Error case Save priority, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Update priority, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
    And match $.code == '301'
