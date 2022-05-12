Feature: CRUD Campaign

  Background:
    * url urlCampaign

  Scenario: Successful case Find campaign by id and consumer id
    * header id-campaign = 1
    * header id-consumer = 2
    When method GET
    Then status 200
    And match $.idCampaign == '1'
    And match $.idConsumer == '2'

  Scenario: Successful case Find all campaigns
    * def urlFind = urlCampaign + "/all-campaign/"
    Given url urlFind
    When method GET
    Then status 200
    And match $[0].idCampaign == '#notnull'

  Scenario: Successful case Delete campaign by id and consumer id
    * header id-campaign = 1
    * header id-consumer = 2
    When method DELETE
    Then status 200
    And match response == '1'

  Scenario: Successful case Save campaign
    * def idCampaign = '1'
    * def idConsumer = '2'
    * def sourcePath = 'sourcePath'
    * def schedule1_id = 1
    * def schedule2_id = 2
    Given request read("../data/campaign.json")
    When method POST
    Then status 200
    And match $.idCampaign == '1'

  Scenario: Successful case Update campaign
    * def idCampaign = '1'
    * def idConsumer = '2'
    * def sourcePath = 'newSourcePath'
    * def schedule1_id = 1
    * def schedule2_id = 2
    Given request read("../data/campaign.json")
    When method PUT
    Then status 200
    And match $.actual.idCampaign == '1'
    And match $.actual.idConsumer == '2'
    And match $.actual.sourcePath == 'newSourcePath'

  Scenario: Error case Find campaign by id and consumer id
    * header id-campaign = 10
    * header id-consumer = 15
    When method GET
    Then status 500
    And match $.code == '384'

  Scenario: Error case Update campaign
    * def idCampaign = 10
    * def idConsumer = 15
    * def idConsumer = '2'
    * def sourcePath = 'newSourcePath'
    * def schedule1_id = 1
    * def schedule2_id = 2
    Given request read("../data/campaign.json")
    When method PUT
    Then status 500
    And match $.code == '384'

  Scenario: Error case Delete campaign by id and consumer id
    * header id-campaign = 10
    * header id-consumer = 15
    When method DELETE
    Then status 500
    And match $.code == '384'

  Scenario: Error case Save campaign, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Update campaign, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
    And match $.code == '301'
