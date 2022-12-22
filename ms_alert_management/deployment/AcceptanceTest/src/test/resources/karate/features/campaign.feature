Feature: CRUD Campaign

  Background:
    * url urlCampaign
    * def body = read("../data/campaign.json")
    * def res = read("../data/campaignResponse.json")

  Scenario: Successful case Save campaign with attachment
    * set body.idCampaign = '1'
    * set body.idConsumer = 'SVP'
    * set body.attachment = true
    * set body.attachmentPath = 'attachmentPath'
    Given request body
    When method POST
    * def file = responseStatus == 200 ? 'campaign-success.feature' : 'campaign-failed.feature'
    * def result = call read(file) response

  Scenario: Successful case Save campaign without attachment
    * set body.idCampaign = '2'
    * set body.idConsumer = 'SVP'
    Given request body
    When method POST
    * def file = responseStatus == 200 ? 'campaign-success.feature' : 'campaign-failed.feature'
    * def result = call read(file) response

  Scenario: Successful case Save campaign without endDate and endTime
    * set body.idCampaign = '3'
    * set body.idConsumer = 'SVP'
    * remove body.schedules[0].endDate
    * remove body.schedules[0].endTime
    Given request body
    When method POST
    * def file = responseStatus == 200 ? 'campaign-success.feature' : 'campaign-failed.feature'
    * def result = call read(file) response

  Scenario: Error case Save campaign with endDate less than startDate
    * set body.idCampaign = '4'
    * set body.idConsumer = 'SVP'
    * set body.schedules[0].endDate = '2022-01-05'
    Given request body
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Save campaign with startTime and endTime fields values don't match
    * set body.idCampaign = '4'
    * set body.idConsumer = 'SVP'
    * set body.schedules[0].endTime = '10:30:00.00'
    Given request body
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Save campaign without schedules
    * set body.idCampaign = '4'
    * set body.idConsumer = 'SVP'
    * set body.schedules = []
    Given request body
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Save campaign, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Successful case Find campaign by id and consumer id
    Given params { 'id-campaign': '1', 'id-consumer': 'SVP' }
    When method GET
    Then status 200
    And match $.data.idCampaign == '1'
    And match $.data.idConsumer == 'SVP'

  Scenario: Error case Find campaign by id and consumer id
    Given params { 'id-campaign': '0', 'id-consumer': 'SVP' }
    When method GET
    Then status 500
    And match $.code == '384'

  Scenario: Error case Find campaign by id and consumer id, missing parameter per body
    Given params {}
    When method GET
    Then status 500
    And match $.code == '301'

  Scenario: Successful case Find all campaigns
    * def urlFind = urlCampaign + "/all"
    Given url urlFind
    When method GET
    Then status 200
    And match $.data[0].idCampaign == '#notnull'
    And match $.data[0].idConsumer == '#notnull'

  Scenario: Successful case Delete campaign by id and consumer id
    Given request { idCampaign: '1', idConsumer: 'SVP' }
    When method DELETE
    Then status 200
    And match $.data.idCampaign == '1'
    And match $.data.idConsumer == 'SVP'

  Scenario: Error case Delete campaign by id and consumer id
    Given request { idCampaign: '0', idConsumer: 'SVP' }
    When method DELETE
    Then status 500
    And match $.code == '384'

  Scenario: Error case Delete campaign, missing parameter per body
    Given request {}
    When method DELETE
    Then status 500
    And match $.code == '301'

  Scenario: Successful case Update campaign
    * set body.idCampaign = '3'
    * set body.idConsumer = 'SVP'
    * set body.sourcePath = 'newSourcePath'
    * remove body.schedules
    Given request body
    When method PUT
    Then status 200
    And match $.data.actual.idCampaign == '3'
    And match $.data.actual.idConsumer == 'SVP'
    And match $.data.actual.sourcePath == 'newSourcePath'

  Scenario: Successful case Activate campaign
    * set body.idCampaign = '2'
    * set body.idConsumer = 'SVP'
    * remove body.schedules
    Given request body
    When method PUT
    Then status 200
    And match $.data.actual.idCampaign == '2'
    And match $.data.actual.idConsumer == 'SVP'
    And match $.data.actual.state == '1'

  Scenario: Error case Update campaign
    * set body.idCampaign = '0'
    * set body.idConsumer = 'SVP'
    * set body.sourcePath = 'newSourcePath'
    Given request body
    When method PUT
    Then status 500
    And match $.code == '384'

  Scenario: Error case Update campaign, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
    And match $.code == '301'