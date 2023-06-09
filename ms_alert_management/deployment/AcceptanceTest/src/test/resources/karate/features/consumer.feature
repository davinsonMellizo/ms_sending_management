Feature: CRUD Consumer

  Background:
    * url urlConsumer

  Scenario: Successful case Save consumer
    * def id = "SAV"
    Given request read("../data/consumer.json")
    When method POST
    Then status 200
    And match $.id == 'SAV'

  Scenario: Successful case Find consumer by id
    * def urlFind = urlConsumer + "/SAV"
    Given url urlFind
    When method GET
    Then status 200
    And match $.id == 'SAV'

  Scenario: Successful case Find all consumers
    When method GET
    Then status 200

  Scenario: Successful case Update consumer
    * def id = "SAV"
    Given request read("../data/consumer.json")
    When method PUT
    Then status 200
    And match $.actual.id == 'SAV'

  Scenario: Successful case Delete consumer by id
    * def urlDelete = urlConsumer + "/SAV"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == 'SAV'

  Scenario: Error case Find consumer by id
    * def urlFind = urlConsumer + "/10"
    Given url urlFind
    When method GET
    Then status 500
    And match $.code == '380'

  Scenario: Error case Update consumer
    * def id = 10
    Given request read("../data/consumer.json")
    When method PUT
    Then status 500
    And match $.code == '380'

  Scenario: Error case Delete consumer by id
    * def urlDelete = urlConsumer + "/10"
    Given url urlDelete
    When method DELETE
    Then status 500
    And match $.code == '380'

  Scenario: Error case Save consumer, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Update consumer, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
    And match $.code == '301'
