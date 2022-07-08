function() {

  karate.configure('connectTimeout', 500000);
  karate.configure('readTimeout', 500000);


  var envData = read('../configurations/paths.json');

  var config = {
      urlSend: ''
      host: '127.0.0.1',
      port: 5672,
      queueName: 'ms_email_management',
      username: 'guest',
      password: 'guest',
  }

  config.urlSend = envData.urlSend;
  return config

}