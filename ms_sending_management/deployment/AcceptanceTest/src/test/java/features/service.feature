@ignore
Feature: CRUD Service

  Background:
    * url urlService

  Scenario: Successful case Find service by id
    * def urlFind = urlService + "/0"
    Given url urlFind
    When method GET
    Then status 200
    And match $.id == 0

  Scenario: Successful case Delete service by id
    * def urlDelete = urlService + "/1"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == '1'

  Scenario: Successful case Save service
    * def id = 3
    Given request read("../data/service.json")
    When method POST
    Then status 200
    And match $.id == 3

  Scenario: Successful case Update service
    * def id = 2
    Given request read("../data/service.json")
    When method PUT
    Then status 200
    And match $.actual.id == 2

  Scenario: Error case Find service by id
    * def urlFind = urlService + "/10"
    Given url urlFind
    When method GET
    Then status 500
    And match $.code == '377'

  Scenario: Error case Update service
    * def id = 10
    Given request read("../data/service.json")
    When method PUT
    Then status 500
    And match $.code == '377'

  Scenario: Error case Delete service by id
    * def urlDelete = urlService + "/10"
    Given url urlDelete
    When method DELETE
    Then status 500
    And match $.code == '377'

  Scenario: Error case Save services, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Update services, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
    And match $.code == '301'

