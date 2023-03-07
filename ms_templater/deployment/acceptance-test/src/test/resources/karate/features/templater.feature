Feature: management of microservice templater

  Background:
    * url templaterUrl
    * def templateBody = read("../data/templateBody.json")
    * def deleteTemplateBody = read("../data/deleteTemplateBody.json")
    * def msgBody = read("../data/messageBody.json")
    * def resp = read("../data/response.json")
    * def msgResp = read("../data/messageResponse.json")

  @PostSuccessfulCase
  Scenario: Create template
    * def pathUser = '/create'
    Given path pathUser
    And request templateBody
    When method post
    * def file = responseStatus == 200 ? 'success.feature' :'failed.feature'
    * def result = call read(file) response

  @PostAlternativeCaseWithTemplateExisting
  Scenario: create failed template existing
    * def pathUser = '/create'
    Given path pathUser
    And request templateBody
    When method post
    Then status 409
    * print response
    And match response.error.title == "TEMPLATE_ALREADY_EXISTS"

  @GetWithId
  Scenario: Get template with id
    * def pathUser = '/get'
    * param idTemplate = '0001'
    Given path pathUser
    When method get
    Then status 200
    * print response
    And match response.data == resp

  @GetWithIdNotFound
  Scenario: Get template not found
    * def pathUser = '/get'
    * param idTemplate = '9876543210987'
    Given path pathUser
    When method get
    Then status 409
    * print response
    And match response.error.title == "TEMPLATE_NOT_FOUND"

  @PostAlternativeCaseWithABadStatus
  Scenario: create template with a bad status
    * set templateBody.status = "2"
    * def pathUser = '/create'
    Given path pathUser
    And request templateBody
    When method post
    Then status 409
    * print response
    And match response.error.title == "TECHNICAL_PARAMETER"

  @DeleteSuccessfulCase
  Scenario: Delete template
    * def pathUser = '/delete'
    Given path pathUser
    And request deleteTemplateBody
    When method put
    Then status 200
    * print response
    And match response.data.before == resp
    And match response.data.current == resp

  @DeleteAlternativeCase
  Scenario: Delete template failed
    * set deleteTemplateBody.idTemplate = "ABC"
    * def pathUser = '/delete'
    Given path pathUser
    And request deleteTemplateBody
    When method put
    Then status 409
    * print response
    And match response.error.title == "TEMPLATE_NOT_FOUND"

  @PutSuccessfulCase
  Scenario: Update template
    * def pathUser = '/update'
    Given path pathUser
    And request templateBody
    When method put
    Then status 200
    * print response
    And match response.data.before == resp
    And match response.data.current == resp

  @PutAlternativeCase
  Scenario: Update template failed
    * set templateBody.status = "2"
    * def pathUser = '/update'
    Given path pathUser
    And request templateBody
    When method put
    Then status 409
    * print response
    And match response.error.title == "TECHNICAL_PARAMETER"

  @CreateMessageSuccessfulCase
  Scenario: create a message with get
    * def pathUser = '/message'
    * param idTemplate = 'TEST_CREATE_MSG'
    Given path pathUser
    And request msgBody
    When method get
    Then status 200
    * print response
    And match response.data == msgResp

  @CreateMessageAlternativeCase
  Scenario: Get an message failed with get
    * def pathUser = '/message'
    * param idTemplate = 'ABC'
    Given path pathUser
    And request msgBody
    When method get
    Then status 409
    * print response
    And match response.error.title == "TEMPLATE_NOT_FOUND"
