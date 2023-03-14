Feature: Send Alert

  Background:
    * url urlSend
    * def body = read("../data/alert.json")
    * configure ssl = true


  Scenario: Successful case send alert to masivian provider
    * set body.provider = "MAS"
    Given request body
    When method POST
    Then status 200

  Scenario: Successful case send sms alert to infobip provider
    * set body.provider = "INF"
    Given request body
    When method POST
    Then status 200

  Scenario: Error case send alert, missing parameter per body
    Given request {}
    When method POST
    Then status 500