@ignore
Feature: Create enroll successful
  Scenario:
    Then assert responseStatus == 200
    And match $.data.idCampaign == res.idCampaign
    And match $.data.idConsumer == res.idConsumer