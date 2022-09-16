function fn(){

    var connectTimeout = karate.properties['connectTimeout'] || 15000
    karate.configure('connectTimeout', connectTimeout);
    karate.configure('readTimeout',connectTimeout);
    karate.configure('ssl',true);

    var envData = read('../data/variable.json');

    var config = {
        templaterUrl: ' ',
    }

    config.templaterUrl = envData.url;

    return config
}
