Feature: Massive

  Background:
    * url urlSendCampaign

  @PostSuccessfully
  Scenario: Successfully start campaign
    * def body =
    """
     {
        "idCampaign": "2",
        "idConsumer": "SVP"
     }
    """

    Given request body
    When method POST
    Then status 200
    And match $.message == 'Campana en ejecucion'

  @PostNotFound
  Scenario: Campaign Not found
    * def body =
    """
     {
        "idCampaign": "0",
        "idConsumer": "SVP"
     }
    """

    Given request body
    When method POST
    Then status 500
    And match $.code == 'BE001'

  @PostWithoutOnDemand
  Scenario: Campaign without schedule on demand
    * def body =
    """
     {
        "idCampaign": "3",
        "idConsumer": "SVP"
     }
    """

    Given request body
    When method POST
    Then status 500
    And match $.code == 'BE002'

  @PostCampaignInactive
  Scenario: Campaign is inactive
    * def body =
    """
     {
        "idCampaign": "1",
        "idConsumer": "SVP"
     }
    """

    Given request body
    When method POST
    Then status 500
    And match $.code == 'BE003'

  @PostParamsEmpty
  Scenario: Failed params empty

    When method POST
    Then status 500
    And match $.code == 'TE001'

  @PostDataNull
  Scenario: Data No null
    * def body =
    """
     {
     }
    """

    Given request body
    When method POST
    Then status 500
    And match $.code == 'TE001'
