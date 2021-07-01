Feature: CRUD client

  Background:
    * url urlClient

  Scenario: Successful case Find Client
    Given header document-number = "1000000000"
    And header document-type = "0"
    When method GET
    Then status 200
    And match $.documentNumber == 1000000000

  Scenario: Successful case Delete Client
    Given header document-number = "1000000001"
    And header document-type = "0"
    When method DELETE
    Then status 200
    And match $.documentNumber == 1000000001

  Scenario: Successful case Save Client
    * def documentNumber = "1000000002"
    Given request read("../data/client.json")
    When method POST
    Then status 200
    And match $.documentNumber == 1000000002

  Scenario: Successful case Update Client
    * def documentNumber = "1000000000"
    Given request read("../data/client.json")
    When method PUT
    Then status 200
    And match $.actual.documentNumber == 1000000000

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
    And match $.code == '373'

  Scenario: Error case Delete Client
    Given header document-number = "1000000010"
    And header document-type = "0"
    When method DELETE
    Then status 500
    And match $.code == '373'

  Scenario: Error case Save client, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Update client, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
    And match $.code == '301'

