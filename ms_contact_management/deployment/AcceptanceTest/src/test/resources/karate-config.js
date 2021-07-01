function() {

  karate.configure('connectTimeout', 500000);
  karate.configure('readTimeout', 500000);


  var envData = read('../configurations/paths.json');

  var config = {
      urlClient: '',
      urlContact: ''
  }

  config.urlClient = envData.urlClient;
  config.urlContact = envData.urlContact;
  return config

}