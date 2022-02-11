function() {

  karate.configure('connectTimeout', 500000);
  karate.configure('readTimeout', 500000);

  var envData = read('../configurations/config_rabbit.json');

  var config = {
      host: '127.0.0.1',
      port: 5672,
      queueName: 'ms_sms_management',
      username: 'guest',
      password: 'guest',
  }

  config.host = envData.host;
  config.port = envData.port;
  config.queueName = envData.queueName;
  config.username = envData.username;
  config.password = envData.password;
  return config

}