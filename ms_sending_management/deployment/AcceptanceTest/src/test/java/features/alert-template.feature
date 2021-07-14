Feature: Create, Read & delete Alert Template

  Background:
    * url urlAlert+"-template"

  Scenario: Successful case Find alertTemplate by id
    * def urlFind = urlAlert+"-template/2"
    Given url urlFind
    When method GET
    Then status 200
    And match $.id == 2

  Scenario: Successful case Save alertTemplate
    * def id = 4
    Given request read("../data/alertTemplate.json")
    When method POST
    Then status 200
    And match $.id == 4

  Scenario: Successful case Delete alertTemplate by id
    * def urlDelete = urlAlert+"-template" + "/4"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == '4'

  Scenario: Error case Find alertTemplate by id
    * def urlFind = urlAlert+"-template" + "/10"
    Given url urlFind
    When method GET
    Then status 500

  Scenario: Error case Delete alertTemplate by id
    * def urlDelete = urlAlert+"-template" + "/10"
    Given url urlDelete
    When method DELETE
    Then status 500

  Scenario: Error case Save services, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'