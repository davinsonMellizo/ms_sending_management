Feature: CRUD and relations with alert

  Background:
    * url urlAlert

  Scenario: Successful prepare data
    * def id = "AFI"
    Given request read("../data/alert.json")
    When method POST
    Then status 200
    And match $.id == 'AFI'

  Scenario: Successful case Find alert by id
    * def urlFind = urlAlert + "/AFI"
    Given url urlFind
    When method GET
    Then status 200
    And match $.id == 'AFI'

  Scenario: Successful case Delete alert by id
    * def urlDelete = urlAlert + "/AFI"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == 'AFI'

  Scenario: Successful case Save alert
    * def id = "SAV"
    Given request read("../data/alert.json")
    When method POST
    Then status 200
    And match $.id == 'SAV'

  Scenario: Successful case Update alert
    * def id = "SAV"
    Given request read("../data/alert.json")
    When method PUT
    Then status 200
    And match $.actual.id == 'SAV'

  Scenario: Error case Find alert by id
    * def urlFind = urlAlert + "/ANF"
    Given url urlFind
    When method GET
    Then status 500
    And match $.code == '120'

  Scenario: Error case Update alert
    * def id = "ANF"
    Given request read("../data/alert.json")
    When method PUT
    Then status 500
    And match $.code == '120'

  Scenario: Error case Delete alert by id
    * def urlDelete = urlAlert + "/ANF"
    Given url urlDelete
    When method DELETE
    Then status 500
    And match $.code == '120'

  Scenario: Error case Save alert, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Update alert, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
    And match $.code == '301'


 Scenario: Successful case Delete prepare data
    * def urlDelete = urlAlert + "/SAV"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == 'SAV'