function() {

  karate.configure('connectTimeout', 500000);
  karate.configure('readTimeout', 500000);
  karate.configure ('ssl', true);

  var envData = read('../configurations/paths.json');

  return {
            urlSendCampaign: envData.urlSendCampaign
          };
}