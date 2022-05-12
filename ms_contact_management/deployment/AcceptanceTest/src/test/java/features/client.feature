Feature: CRUD client

  Background:
    * url urlClient
    * def result = callonce read('../features/test.feature')

  Scenario: Successful case Find Client
    Given header document-number = result.value
    And header document-type = "0"
    When method GET
    Then status 200
    Then print result.value
    And match $.documentNumber == Number(result.value)

  Scenario: Successful case Update Client
    * def consumer = "ALM"
    * def documentNumber = result.value
    Given request read("../data/client-update.json")
    When method PUT
    Then status 200
    Then print result.value
    And match $.idResponse == '120'

  Scenario: Error case Find Client
    Given header document-number = "1000000010"
    And header document-type = "0"
    When method GET
    Then status 500
    And match $.code == '373'

  Scenario: Error case Update Client
    * def documentNumber = "1000000010"
    Given request read("../data/client.json")
    When method PUT
    Then status 500
    And match $.code == '301'

  Scenario: Error case Save client, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == 'DST0012'

  Scenario: Error case Update client, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
    And match $.code == 'DST0012'

