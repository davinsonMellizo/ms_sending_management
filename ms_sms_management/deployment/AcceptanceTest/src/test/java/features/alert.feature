Feature: Send Alert

  Background:
    * url urlSend
    * def body = read("../data/alert.json")

  Scenario: Successful case send alert
    Given request body
    When method POST
    Then status 200

  Scenario: Successful case send sms alert to infobip provider
    * set body.alertParameters.alert = "201"
    Given request body
    When method POST
    Then status 200

  Scenario: Error case send alert, missing parameter per body
    Given request {}
    When method POST
    Then status 500