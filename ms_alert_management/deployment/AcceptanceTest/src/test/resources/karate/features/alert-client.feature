Feature: CRUD alert client

  Background:
    * url urlAlert + "-client"

  Scenario: Successful case Find all relations alert with client
    Given header document-number = "1111111111"
    And header document-type = "0"
    When method GET
    Then status 200

  Scenario: Successful case Delete relation alert with client
    Given header id-alert = "I01"
    And header document-number = "1111111111"
    And header document-type = "0"
    When method DELETE
    Then status 200
    And match $.documentNumber == 1111111111

  Scenario: Successful case Save relation alert with client
    * def idAlert = "I01"
    Given request read("../data/alertClient.json")
    When method POST
    Then status 200
    And match $.idAlert == 'I01'

  Scenario: Error case Save relation alert with client, alert not found
    * def idAlert = "5a"
    Given request read("../data/alertClient.json")
    When method POST
    Then status 500

  Scenario: Error case Delete relation alert with client
    Given header id-alert = "5a"
    And header document-number = "9632"
    And header document-type = "8"
    When method DELETE
    Then status 500
    And match $.code == '378'

  Scenario: Error case Save alert client, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Delete alert client, missing parameter per header
    When method DELETE
    Then status 500
    And match $.code == '301'
