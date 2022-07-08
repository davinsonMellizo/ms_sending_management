@ignore
Feature: re-usable feature to create a single document

  Background:
    * url urlClient
    * def random =
    """
      function(s) {
         var text = "";
         var aux = "0123456789";
         for (var i = 0; i < s; i++)
           text += aux.charAt(Math.floor(Math.random() * aux.length));
         return text;
      }
    """
    * def value = random(10)

  Scenario: Successful case Save Client
    * def documentNumber = value
    * def consumer = "ALM"
    Given request read("../data/client.json")
    When method POST
    Then status 200
    And match $.idResponse == "120"