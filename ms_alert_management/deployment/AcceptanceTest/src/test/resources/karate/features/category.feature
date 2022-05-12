Feature: CRUD and relations with category

  Background:
    * url urlCategory

  Scenario: Successful case Find category by id
    * def urlFind = urlCategory + "/0"
    Given url urlFind
    When method GET
    Then status 200
    And match $.id == 0

  Scenario: Successful case Delete category by id
    * def urlDelete = urlCategory + "/6"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == "6"

  Scenario: Successful case Save category
    * def id = 5
    Given request read("../data/category.json")
    When method POST
    Then status 200
    And match $.id == 5

  Scenario: Successful case Update category
    * def id = 7
    Given request read("../data/category.json")
    When method PUT
    Then status 200
    And match $.actual.id == 7

  Scenario: Error case Find category by id
    * def urlFind = urlCategory + "/8"
    Given url urlFind
    When method GET
    Then status 500
    And match $.code == '381'

  Scenario: Error case Update category
    * def id = 20
    Given request read("../data/category.json")
    When method PUT
    Then status 500
    And match $.code == '381'

  Scenario: Error case Delete category by id
    * def urlDelete = urlCategory + "/10"
    Given url urlDelete
    When method DELETE
    Then status 500
    And match $.code == '381'

  Scenario: Error case Save category, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Update category, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
    And match $.code == '301'
