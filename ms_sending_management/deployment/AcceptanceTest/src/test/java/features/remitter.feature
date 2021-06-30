Feature: CRUD Remitter

  Background:
    * url urlRemitter

  Scenario: Successful case Find remitter by id
    * def urlFind = urlRemitter + "/0"
    Given url urlFind
    When method GET
    Then status 200
    And match $.id == 0

  Scenario: Successful case Find all remitters
    When method GET
    Then status 200
    And match $[0].id == 0

  Scenario: Successful case Delete remitter by id
    * def urlDelete = urlRemitter + "/1"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == '1'

  Scenario: Successful case Save remitter
    * def id = 3
    Given request read("../data/remitter.json")
    When method POST
    Then status 200
    And match $.id == 3

  Scenario: Successful case Update remitter
    * def id = 2
    Given request read("../data/remitter.json")
    When method PUT
    Then status 200
    And match $.actual.id == 2

  Scenario: Error case Find remitter by id
    * def urlFind = urlRemitter + "/10"
    Given url urlFind
    When method GET
    Then status 500
    And match $.code == '375'

  Scenario: Error case Update remitter
    * def id = 10
    Given request read("../data/remitter.json")
    When method PUT
    Then status 500
    And match $.code == '375'

  Scenario: Error case Delete remitter by id
    * def urlDelete = urlRemitter + "/10"
    Given url urlDelete
    When method DELETE
    Then status 500
    And match $.code == '375'

  Scenario: Error case Save remitter, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Update remitter, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
    And match $.code == '301'