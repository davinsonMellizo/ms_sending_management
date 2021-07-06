function() {

  karate.configure('connectTimeout', 500000);
  karate.configure('readTimeout', 500000);


  var envData = read('../configurations/paths.json');

  var config = {
      urlAlert: '',
      urlRemitter: '',
      urlService: '',
      urlProvider: ''
  }

  config.urlAlert = envData.urlAlert;
  config.urlRemitter = envData.urlRemitter;
  config.urlService = envData.urlService;
  config.urlProvider = envData.urlProvider;
  return config

}