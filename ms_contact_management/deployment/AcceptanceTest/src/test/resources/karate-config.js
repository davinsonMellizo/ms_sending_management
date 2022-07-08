function() {

  karate.configure('connectTimeout', 500000);
  karate.configure('readTimeout', 500000);
  karate.configure ('ssl', true);


  var envData = read('../configurations/paths.json');

  var config = {
      urlClient: '',
  }

  config.urlClient = envData.urlClient;
  return config

}
