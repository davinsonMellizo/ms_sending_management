Feature: CRUD Schedule

  Background:
    * url urlSchedule
    * def body = read("../data/schedule.json")

  Scenario: Successful case Save schedule with massive
    * set body.idCampaign = '2'
    * set body.idConsumer = 'SVP'
    * set body.scheduleType = 'DAILY'
    Given request body
    When method POST
    Then status 200
    And match $.data.idCampaign == '2'
    And match $.data.idConsumer == 'SVP'

  Scenario: Successful case Save schedule without endDate and endTime
    * set body.idCampaign = '2'
    * set body.idConsumer = 'SVP'
    * set body.scheduleType = 'WEEKLY'
    * remove body.endDate
    * remove body.endTime
    Given request body
    When method POST
    Then status 200
    And match $.data.idCampaign == '2'
    And match $.data.idConsumer == 'SVP'

  Scenario: Error case Save schedule with campaign that does not exist
    * set body.idCampaign = '0'
    * set body.idConsumer = 'SVP'
    * set body.scheduleType = 'DAILY'
    Given request body
    When method POST
    Then status 500
    And match $.code == '384'

  Scenario: Error case Save schedule with endDate less than startDate
    * set body.idCampaign = '1'
    * set body.idConsumer = 'SVP'
    * set body.endDate = '2022-01-05'
    * set body.scheduleType = 'DAILY'
    Given request body
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Save schedule with startTime and endTime fields values don't match
    * set body.idCampaign = '1'
    * set body.idConsumer = 'SVP'
    * set body.endTime = '10:25:00.00'
    * set body.scheduleType = 'DAILY'
    Given request body
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Save schedule, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Successful case Find schedule by id
    * def urlFindById = urlSchedule + "/1"
    Given url urlFindById
    When method GET
    Then status 200
    And match $.data.id == 1
    And match $.data.idConsumer == 'SVP'

  Scenario: Error case Find schedule by id
    * def urlFindById = urlSchedule + "/0"
    Given url urlFindById
    When method GET
    Then status 500
    And match $.code == '386'

  Scenario: Error case Find schedule missing id
    * def urlFindById = urlSchedule + "/"
    Given url urlFindById
    When method GET
    Then status 500
    And match $.code == '302'

  Scenario: Successful case Update campaign
    * def urlFindById = urlSchedule + "/2"
    * set body.idCampaign = '4'
    * set body.idConsumer = 'SVP'
    * set body.scheduleType = 'WEEKLY'
    Given url urlFindById
    And request body
    When method PUT
    Then status 200
    And match $.data.actual.id == 2
    And match $.data.actual.scheduleType == 'WEEKLY'

  Scenario: Error case Update schedule
    * def urlFindById = urlSchedule + "/0"
    * set body.idCampaign = '0'
    * set body.idConsumer = 'SVP'
    * set body.scheduleType = 'WEEKLY'
    Given url urlFindById
    And request body
    When method PUT
    Then status 500
    And match $.code == '386'

  Scenario: Error case Update schedule, missing parameter per body
    * def urlFindById = urlSchedule + "/1"
    Given url urlFindById
    And request {}
    When method PUT
    Then status 500
    And match $.code == '301'