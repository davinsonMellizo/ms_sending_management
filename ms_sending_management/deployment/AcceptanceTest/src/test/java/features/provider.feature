@ignore
Feature: CRUD Provider

  Background:
    * url urlProvider

  Scenario: Successful case Find provider by id
    * def urlFind = urlProvider + "/TOD"
    Given url urlFind
    When method GET
    Then status 200
    And match $.id == 'TOD'

  Scenario: Successful case Find all providers
    When method GET
    Then status 200
    And match $[0].id == 'TOD'

  Scenario: Successful case Delete provider by id
    * def urlDelete = urlProvider + "/PDI"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == 'PDI'

  Scenario: Successful case Save provider
    * def id = "PSA"
    Given request read("../data/provider.json")
    When method POST
    Then status 200
    And match $.id == 'PSA'

  Scenario: Successful case Update provider
    * def id = "TOD"
    Given request read("../data/provider.json")
    When method PUT
    Then status 200
    And match $.actual.id == 'TOD'

  Scenario: Error case Find provider by id
    * def urlFind = urlProvider + "/PNF"
    Given url urlFind
    When method GET
    Then status 500
    And match $.code == '376'

  Scenario: Error case Update provider
    * def id = "PNF"
    Given request read("../data/provider.json")
    When method PUT
    Then status 500
    And match $.code == '376'

  Scenario: Error case Delete provider by id
    * def urlDelete = urlProvider + "/PNF"
    Given url urlDelete
    When method DELETE
    Then status 500
    And match $.code == '376'

  Scenario: Error case Save provider, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Successful case Update provider, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
    And match $.code == '301'

