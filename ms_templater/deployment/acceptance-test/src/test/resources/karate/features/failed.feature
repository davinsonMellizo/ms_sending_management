@ignore
Feature: connection failed with SNS
  Scenario:
    Then assert responseStatus == 409
    And match $.error.title == '#notnull'