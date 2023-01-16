Feature: CRUD Provider

  Background:
    * url urlProvider

  Scenario: Successful prepare data
    * def id = "TO1"
    Given request read("../data/provider.json")
    When method POST
    Then status 200
    And match $.id == 'TO1'

  Scenario: Successful case Find provider by id
    * def urlFind = urlProvider + "/MAS"
    Given url urlFind
    When method GET
    Then status 200
    And match $.id == 'MAS'

  Scenario: Successful case Find all providers
    When method GET
    Then status 200


  Scenario: Successful case Update provider
    * def id = "TO1"
    Given request read("../data/provider.json")
    When method PUT
    Then status 200
    And match $.actual.id == 'TO1'

  Scenario: Successful case Delete provider by id
    * def urlDelete = urlProvider + "/TO1"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == 'TO1'

  Scenario: Successful case Save provider
    * def id = "PSA"
    Given request read("../data/provider.json")
    When method POST
    Then status 200
    And match $.id == 'PSA'

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

  Scenario: Successful case Delete prepare data
    * def urlDelete = urlProvider + "/PSA"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == 'PSA'