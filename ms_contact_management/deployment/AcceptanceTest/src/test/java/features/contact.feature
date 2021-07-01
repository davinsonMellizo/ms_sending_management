Feature: CRUD contact

  Background:
    * url urlContact

  Scenario: Successful case Find Contacts by client
    Given header document-number = "1000000000"
    And header document-type = "0"
    When method GET
    Then status 200
    And match $[0].documentNumber == 1000000000

  Scenario: Successful case Delete Contact
    Given header document-number = "1000000000"
    And header document-type = "0"
    And header contact-medium = "SMS"
    And header enrollment-contact = "ALM"
    When method DELETE
    Then status 200

  Scenario: Successful case Save Contact
    * def enrollmentContact = "VLP"
    Given request read("../data/contact.json")
    When method POST
    Then status 200
    And match $.documentNumber == 1000000000

  Scenario: Successful case Update Contact
    * def enrollmentContact = "SVP"
    Given request read("../data/contact.json")
    When method PUT
    Then status 200
    And match $.actual.documentNumber == 1000000000

  Scenario: Error case Update Contact
    * def enrollmentContact = "NOT"
    Given request read("../data/contact.json")
    When method PUT
    Then status 500
    And match $.code == '374'

  Scenario: Error case Delete Contact
    Given header document-number = "1000000010"
    And header document-type = "0"
    And header contact-medium = "SMS"
    And header enrollment-contact = "NOT"
    When method DELETE
    Then status 500
    And match $.code == '374'

  Scenario: Error case Save contact, missing parameter per body
    Given request {}
    When method POST
    Then status 500
    And match $.code == '301'

  Scenario: Error case Update contact, missing parameter per body
    Given request {}
    When method PUT
    Then status 500
    And match $.code == '301'

