Feature: CRUD Provider

  Background:
    * def config = { host: '#(host)' , port: '#(port)', username: '#(username)', password: '#(password)', queueName: '#(queueName)'}

  Scenario: Send message to RabbitMQ queue
    * def alertJson = karate.readAsString("../data/request.json")
    * def utilsRabbit = Java.type('utils.UtilsRabbit')
    Given utilsRabbit.send(config, alertJson)

