Feature: management of microservice templater

      Background:
        * url api.reqresURL
        * def random =
          """
              function (min, max) {
              return Math.floor(Math.random() * (max - min)) + min;
              }
          """

      @GetWithId
      Scenario: Get an user with id
        * def pathUser = '/api/v1/templater/get'
        * param idTemplate = '0001'
        Given path pathUser
        When method get
        Then status 200
        * print response
        * def jsonValidation =
        """
          {
          "meta": {
            "_version": "0.0.1",
            "_requestDate": "#string",
            "_responseSize": 1,
            "_requestClient": "/get"
          },
          "data": {
            "idTemplate": "0001",
            "messageType": "EMAIL",
            "version": "1",
            "idConsumer": "1",
            "description": "EMAIL TEMPLATE",
            "messageSubject": "EMAIL",
            "messageBody": "HTML",
            "plainText": "PLAIN",
            "creationUser": "MAQUIJAN",
            "creationDate": "2022-08-18 09:25:56.079",
            "modificationUser": "MAQUIJAN",
            "modificationDate": "2022-08-18 14:32:36.750",
            "status": "0"
          }
        }
        """
        And match response == jsonValidation


      @GetWithIdDon'tFound
      Scenario: Get an user with id but don't found
        * def pathUser = '/api/v1/templater/get'
        * param idTemplate = '0002'
        Given path pathUser
        When method get
        Then status 409
        * print response
        * def jsonValidation =
        """
          {
            "meta": {
              "_version": "0.0.1",
              "_requestDate": "#string",
              "_responseSize": 1,
              "_requestClient": "/get"
            },
            "error": {
              "code": "B001",
              "type": "Business",
              "title": "TEMPLATE_NOT_FOUND",
              "reason": "Template Not Found",
              "detail": "Template Not Found",
              "source": "ms_templater"
            }
          }
        """
        And match response == jsonValidation


      @PostHappyCase
        Scenario: create a user
          * def idTemplater = '000' + random(10,19)
          * def info =
          """
          {
            "idTemplate": "#(idTemplater)",
            "messageType": "SMS",
            "version": "1",
            "idConsumer": "1",
            "description": "SMS TEMPLATE",
            "messageSubject": "SMS",
            "messageBody": "HTML",
            "plainText": "PLAIN",
            "user": "MAQUIJAN",
            "status": "1"
          }
          """
          * print info
          * def pathUser = '/api/v1/templater/create'
          Given path pathUser
          And request info
          When method post
          Then status 200
          * print response
          * def jsonValidation =
          """
          {
            "meta": {
              "_version": "0.0.1",
              "_requestDate": "#string",
              "_responseSize": 1,
              "_requestClient": "/create"
            },
            "data": {
              "idTemplate": "#(idTemplater)",
              "messageType": "SMS",
              "version": "1",
              "idConsumer": "1",
              "description": "SMS TEMPLATE",
              "messageSubject": "SMS",
              "messageBody": "HTML",
              "plainText": "PLAIN",
              "creationUser": "MAQUIJAN",
              "creationDate": "#string",
              "modificationUser": "MAQUIJAN",
              "modificationDate": "#string",
              "status": "1"
             }
            }
          """
          And match response == jsonValidation


      @PostAlternativeCaseWithTemplateExisting
        Scenario: create failed a user with template existing
          * def info =
          """
          {
            "idTemplate": "0001",
            "messageType": "SMS",
            "version": "1",
            "idConsumer": "1",
            "description": "SMS TEMPLATE",
            "messageSubject": "SMS",
            "messageBody": "HTML",
            "plainText": "PLAIN",
            "user": "MAQUIJAN",
            "status": "1"
          }
          """
          * print info
          * def pathUser = '/api/v1/templater/create'
          Given path pathUser
          And request info
          When method post
          Then status 409
          * print response
          * def jsonValidation =
          """
          {
            "meta": {
            "_version": "0.0.1",
            "_requestDate": "#string",
            "_responseSize": 1,
            "_requestClient": "/create"
          },
          "error": {
            "code": "B002",
            "type": "Business",
            "title": "TEMPLATE_ALREADY_EXISTS",
            "reason": "Existing Template With Requested Parameters",
            "detail": "Existing Template With Requested Parameters",
            "source": "ms_templater"
           }
          }
          """
          And match response == jsonValidation


      @PostAlternativeCaseWithABadStatus
        Scenario: create a user with a bad status
          * def info =
        """
        {
          "idTemplate": "0001",
          "messageType": "SMS",
          "version": "1",
          "idConsumer": "1",
          "description": "SMS TEMPLATE",
          "messageSubject": "SMS",
          "messageBody": "HTML",
          "plainText": "PLAIN",
          "user": "MAQUIJAN",
          "status": "2"
        }
        """
          * print info
          * def pathUser = '/api/v1/templater/create'
          Given path pathUser
          And request info
          When method post
          Then status 409
          * print response
          * def jsonValidation =
        """
        {
          "meta": {
            "_version": "0.0.1",
            "_requestDate": "#string",
            "_responseSize": 1,
            "_requestClient": "/create"
          },
          "error": {
            "code": "T003",
            "type": "Technical",
            "title": "TECHNICAL_PARAMETER",
            "reason": "Technical Error In Parameters",
            "detail": "status: valor debe ser 0 o 1",
            "source": "ms_templater"
          }
        }
        """
        And match response == jsonValidation


      @PutHappyCase
      Scenario: Update a user
        * def info =
        """
        {
          "idTemplate": "0001",
          "messageType": "EMAIL",
          "version": "1",
          "idConsumer": "1",
          "description": "EMAIL TEMPLATE",
          "messageSubject": "EMAIL",
          "messageBody": "HTML",
          "plainText": "PLAIN",
          "user": "VJOJOA",
          "status": "1"
        }
        """
        * def pathUser = '/api/v1/templater/update'
        Given path pathUser
        And request info
        When method put
        Then status 200
        * print response
        * def jsonValidation =
          """
            {
              "meta": {
                "_version": "0.0.1",
                "_requestDate": "#string",
                "_responseSize": 1,
                "_requestClient": "/update"
              },
              "data": {
                "before": {
                  "idTemplate": "0001",
                  "messageType": "EMAIL",
                  "version": "1",
                  "idConsumer": "1",
                  "description": "EMAIL TEMPLATE",
                  "messageSubject": "EMAIL",
                  "messageBody": "HTML",
                  "plainText": "PLAIN",
                  "creationUser": "MAQUIJAN",
                  "creationDate": "#string",
                  "modificationUser": "VJOJOA",
                  "modificationDate": "#string",
                  "status": "1"
                },
                "current": {
                  "idTemplate": "0001",
                  "messageType": "EMAIL",
                  "version": "1",
                  "idConsumer": "1",
                  "description": "EMAIL TEMPLATE",
                  "messageSubject": "EMAIL",
                  "messageBody": "HTML",
                  "plainText": "PLAIN",
                  "creationUser": "MAQUIJAN",
                  "creationDate": "#string",
                  "modificationUser": "VJOJOA",
                  "modificationDate": "#string",
                  "status": "1"
                }
              }
            }
          """
        And match response == jsonValidation


      @PutAlternativeCase
      Scenario: Update failed a user
        * def info =
        """
        {
          "idTemplate": "0001",
          "messageType": "EMAIL",
          "version": "1",
          "idConsumer": "1",
          "description": "EMAIL TEMPLATE",
          "messageSubject": "EMAIL",
          "messageBody": "HTML",
          "plainText": "PLAIN",
          "user": "VJOJOA",
          "status": "2"
        }
        """
        * def pathUser = '/api/v1/templater/update'
        Given path pathUser
        And request info
        When method put
        Then status 409
        * print response
        * def jsonValidation =
        """
        {
          "meta": {
            "_version": "0.0.1",
            "_requestDate": "#string",
            "_responseSize": 1,
            "_requestClient": "/update"
          },
          "error": {
            "code": "T003",
            "type": "Technical",
            "title": "TECHNICAL_PARAMETER",
            "reason": "Technical Error In Parameters",
            "detail": "status: valor debe ser 0 o 1",
            "source": "ms_templater"
          }
        }
        """
        And match response == jsonValidation


      @PutDeleteHappyCase
      Scenario: Delete a user with put
        * def info =
        """
        {
         "idTemplate": "0001",
         "user": "MAQUIJAN"
        }
        """
        * def pathUser = '/api/v1/templater/delete'
        Given path pathUser
        And request info
        When method put
        Then status 200
        * print response
        * def jsonValidation =
        """
        {
          "meta": {
            "_version": "0.0.1",
            "_requestDate": "#string",
            "_responseSize": 1,
            "_requestClient": "/delete"
          },
          "data": {
             "before": {
                "idTemplate": "0001",
                "messageType": "EMAIL",
                "version": "1",
                "idConsumer": "1",
                "description": "EMAIL TEMPLATE",
                "messageSubject": "EMAIL",
                "messageBody": "HTML",
                "plainText": "PLAIN",
                "creationUser": "MAQUIJAN",
                "creationDate": "#string",
                "modificationUser": "MAQUIJAN",
                "modificationDate": "#string",
                "status": "0"
             },
             "current": {
                "idTemplate": "0001",
                "messageType": "EMAIL",
                "version": "1",
                "idConsumer": "1",
                "description": "EMAIL TEMPLATE",
                "messageSubject": "EMAIL",
                "messageBody": "HTML",
                "plainText": "PLAIN",
                "creationUser": "MAQUIJAN",
                "creationDate": "#string",
                "modificationUser": "MAQUIJAN",
                "modificationDate": "#string",
                "status": "0"
             }
          }
        }
        """
        And match response == jsonValidation


      @PutDeleteAlternativeCase #modificar el json con datos alternos
      Scenario: Delete failed a user with put
        * def info =
        """
        {
         "idTemplate": "0002",
         "user": "MAQUIJAN"
        }
        """
        * def pathUser = '/api/v1/templater/delete'
        Given path pathUser
        And request info
        When method put
        Then status 409
        * print response
        * def jsonValidation =
        """
        {
          "meta": {
            "_version": "0.0.1",
            "_requestDate": "#string",
            "_responseSize": 1,
            "_requestClient": "/delete"
          },
          "error": {
            "code": "B001",
            "type": "Business",
            "title": "TEMPLATE_NOT_FOUND",
            "reason": "Template Not Found",
            "detail": "Template Not Found",
            "source": "ms_templater"
          }
        }
        """
        And match response == jsonValidation


      @CreateMessageWithMethodGetHappyCase
      Scenario: create a message with get
        * def pathUser = '/api/v1/templater/get'
        * param idTemplate = '0001'
        Given path pathUser
        When method get
        Then status 200
        * print response
        * def jsonValidation =
        """
        {
          "meta": {
            "_version": "0.0.1",
            "_requestDate": "#string",
            "_responseSize": 1,
            "_requestClient": "/get"
          },
          "data": {
            "idTemplate": "0001",
            "messageType": "EMAIL",
            "version": "1",
            "idConsumer": "1",
            "description": "EMAIL TEMPLATE",
            "messageSubject": "EMAIL",
            "messageBody": "HTML",
            "plainText": "PLAIN",
            "creationUser": "MAQUIJAN",
            "creationDate": "#string",
            "modificationUser": "VJOJOA",
            "modificationDate": "#string",
            "status": "1"
          }
        }
        """
        And match response == jsonValidation


      @CreateMessageWithMethodGetAlternativeCase
      Scenario: Get an message failed with get
        * def pathUser = '/api/v1/templater/get'
        * param idTemplate = '0002'
        Given path pathUser
        When method get
        Then status 409
        * print response
        * def jsonValidation =
        """
        {
          "meta": {
            "_version": "0.0.1",
            "_requestDate": "#string",
            "_responseSize": 1,
            "_requestClient": "/get"
          },
            "error": {
            "code": "B001",
            "type": "Business",
            "title": "TEMPLATE_NOT_FOUND",
            "reason": "Template Not Found",
            "detail": "Template Not Found",
            "source": "ms_templater"
          }
        }
        """
        And match response == jsonValidation
