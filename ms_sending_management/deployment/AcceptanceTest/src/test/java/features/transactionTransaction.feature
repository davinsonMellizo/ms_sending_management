Feature: CRUD alert transaction

  Background:
    * url urlAlert+"-transaction"

  Scenario: Successful case Find all relations alert with transaction and consumer by id alert
    * def urlFind = urlAlert+"-transaction/AFI"
    Given url urlFind
    When method GET
    Then status 200
    And match $[0].idAlert == 'AFI'

  Scenario: Successful case Delete relation alert with transaction and consumer
    Given header id-alert = "AFI"
    And header id-transaction = "0538"
    And header id-consumer = "ALM"
    When method DELETE
    Then status 200
    And match response == 'AFI'

  Scenario: Successful case Save relation alert with transaction and consumer
    * def idAlert = "AFI"
    Given request read("../data/alertTransaction.json")
    When method POST
    Then status 200
    And match $.idAlert == 'AFI'

  Scenario: Error case Save relation alert with transaction and consumer, alert not found
    * def idAlert = "ANT"
    Given request read("../data/alertTransaction.json")
    When method POST
    Then status 500

  Scenario: Error case Delete relation alert with transaction and consumer, alert nor found
    Given header id-alert = "ATN"
    And header id-transaction = "0538"
    And header id-consumer = "ALM"
    When method DELETE
    Then status 500
    And match $.code == '374'

  Scenario: Error case Save alert transaction, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Delete alert transaction, missing parameter per body
    When method DELETE
    Then status 500
    And match $.code == '301'

