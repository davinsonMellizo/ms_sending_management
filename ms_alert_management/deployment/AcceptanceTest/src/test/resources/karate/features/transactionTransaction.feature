Feature: CRUD alert transaction

  Background:
    * url urlAlert

  Scenario: Successful prepare data
    * def id = "AFI"
    Given request read("../data/alert.json")
    When method POST
    Then status 200

  Scenario: Successful case Save relation alert with transaction and consumer
    * url urlAlert + "-transaction"
    * def idAlert = "AFI"
    Given request read("../data/alertTransaction.json")
    When method POST
    Then status 200
    And match $.idAlert == 'AFI'

  Scenario: Successful case Find all relations alert with transaction and consumer by id alert
    * url urlAlert + "-transaction"
    * def urlFind = urlAlert + "-transaction/AFI"
    Given url urlFind
    When method GET
    Then status 200

  Scenario: Successful case Delete relation alert with transaction and consumer
    * url urlAlert + "-transaction"
    Given header id-alert = "AFI"
    And header id-transaction = "0258"
    And header id-consumer = "VLP"
    When method DELETE
    Then status 200
    And match response == 'AFI'

  Scenario: Error case Save relation alert with transaction and consumer, alert not found
    * url urlAlert + "-transaction"
    * def idAlert = "ANT"
    Given request read("../data/alertTransaction.json")
    When method POST
    Then status 500

  Scenario: Error case Delete relation alert with transaction and consumer, alert nor found
    * url urlAlert + "-transaction"
    Given header id-alert = "ATN"
    And header id-transaction = "0538"
    And header id-consumer = "ALM"
    When method DELETE
    Then status 500
    And match $.code == '374'

  Scenario: Error case Save alert transaction, missing parameter per body
    * url urlAlert + "-transaction"
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Delete alert transaction, missing parameter per body
    * url urlAlert + "-transaction"
    When method DELETE
    Then status 500
    And match $.code == '301'

  Scenario: Delete prepare data
    * def urlDelete = urlAlert + "/AFI"
    Given url urlDelete
    When method DELETE
    Then status 200
    And match response == 'AFI'