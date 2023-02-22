function() {

  karate.configure('connectTimeout', 500000);
  karate.configure('readTimeout', 500000);
  karate.configure ('ssl', true);

  var envData = read('../configurations/paths.json');

  var config = {
      urlSend: ''
  }

  config.urlSend = envData.urlSend;
  return config

}