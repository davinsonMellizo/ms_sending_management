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
    And match $[0].id == '375'

  Scenario: Successful case Delete provider by id
    * def urlDelete = urlProvider + "/1"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == '1'

  Scenario: Successful case Save provider
    * def id = 3
    Given request read("../data/provider.json")
    When method POST
    Then status 200
    And match $.id == 3

  Scenario: Successful case Update provider
    * def id = 2
    Given request read("../data/provider.json")
    When method PUT
    Then status 200
    And match $.actual.id == 2

  Scenario: Error case Find provider by id
    * def urlFind = urlProvider + "/10"
    Given url urlFind
    When method GET
    Then status 500
    And match $.code == '375'

  Scenario: Error case Update provider
    * def id = 10
    Given request read("../data/provider.json")
    When method PUT
    Then status 500
    And match $.code == '375'

  Scenario: Error case Delete provider by id
    * def urlDelete = urlProvider + "/10"
    Given url urlDelete
    When method DELETE
    Then status 500
    And match $.code == '375'

