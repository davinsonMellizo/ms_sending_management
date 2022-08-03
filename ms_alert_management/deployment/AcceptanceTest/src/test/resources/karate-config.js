function() {

  karate.configure('connectTimeout', 500000);
  karate.configure('readTimeout', 500000);
  karate.configure ('ssl', true);

  var envData = read('../configurations/paths.json');

  var config = {
      urlAlert: '',
      urlRemitter: '',
      urlProvider: '',
      urlCategory: '',
      urlPriority: '',
      urlConsumer: '',
      urlCampaign: ''
  }

  config.urlAlert = envData.urlAlert;
  config.urlRemitter = envData.urlRemitter;
  config.urlProvider = envData.urlProvider;
  config.urlCategory = envData.urlCategory;
  config.urlPriority = envData.urlPriority;
  config.urlConsumer = envData.urlConsumer;
  config.urlCampaign = envData.urlCampaign;

  return config

}