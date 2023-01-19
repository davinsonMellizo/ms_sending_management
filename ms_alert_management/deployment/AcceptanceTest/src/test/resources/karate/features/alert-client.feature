Feature: CRUD alert client

  Background:
    * url urlAlert

  Scenario: Successful prepare data
    * def id = "AFI"
    Given request read("../data/alert.json")
    When method POST
    Then status 200

  Scenario: Successful case Save relation alert with client
    * url urlAlert + "-client"
    * def idAlert = "AFI"
    Given request read("../data/alertClient.json")
    When method POST
    Then status 200
    And match $.idAlert == 'AFI'

  Scenario: Successful case Find all relations alert with client
    * url urlAlert + "-client"
    Given header document-number = "1111111112"
    And header document-type = "0"
    When method GET
    Then status 200

  Scenario: Successful case Delete relation alert with client
    * url urlAlert + "-client"
    Given header id-alert = "AFI"
    And header document-number = "1111111112"
    And header document-type = "0"
    When method DELETE
    Then status 200
    And match $.documentNumber == 1111111112

  Scenario: Error case Save relation alert with client, alert not found
    * def idAlert = "5a"
    Given request read("../data/alertClient.json")
    When method POST
    Then status 500

  Scenario: Error case Delete relation alert with client
    * url urlAlert + "-client"
    Given header id-alert = "5a"
    And header document-number = "9632"
    And header document-type = "8"
    When method DELETE
    Then status 500
    And match $.code == '378'

  Scenario: Error case Save alert client, missing parameter per body
    * url urlAlert + "-client"
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Delete alert client, missing parameter per header
    * url urlAlert + "-client"
    When method DELETE
    Then status 500
    And match $.code == '301'

  Scenario: Delete prepare data
    * def urlDelete = urlAlert + "/AFI"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == 'AFI'