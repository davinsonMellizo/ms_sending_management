Feature: Find Logs

  Background:
    * url urlLog

  Scenario: Successful case Find logs by document
    Given headers { document-number : '2000000000', document-type: '0' }
    When method GET
    Then status 200
    And match $[0].documentNumber == 2000000000

  Scenario: Successful case Find logs by document and date
    Given headers { document-number : '2000000000', document-type: '0', start-date: '2022-10-04T10:11', end-date:  '2022-10-06T10:11'}
    When method GET
    Then status 200
    And match $[0].documentNumber == 2000000000

  Scenario: Successful case Find logs by document, date and contact
    Given headers { document-number : '2000000000', document-type: '0', start-date: '2022-10-04T10:11', end-date: '2022-10-06T10:11', contact: '3215058449'}
    When method GET
    Then status 200
    And match $[0].documentNumber == 2000000000

  Scenario: Successful case Find logs by all filters
    Given headers { document-number : '2000000000', document-type: '0', start-date: '2022-10-04T10:11', end-date:  '2022-10-06T10:11', contact: '3215058449', provider: 'TD1', consumer: 'SVP'}
    When method GET
    Then status 200
    And match $[0].documentNumber == 2000000000

