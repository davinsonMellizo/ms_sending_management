function () {
    karate.log('karate.env system property was:', karate.env);
    karate.configure('connectTimeout', 500000);
    karate.configure('readTimeout', 500000);
    karate.configure('ssl', true);

    var envData = read('../data/variable.json');

    return {
        aws: {
            region: 'us-east-1',
            s3: {
                bucketNameSource: envData.bucketNameSource,
                bucketNameTarget: envData.bucketNameTarget,
                bucketProcessedMasiv: envData.bucketProcessedMasiv,
                massivePrefix: envData.massivePrefix,
                massiveErrorPrefix: envData.massiveErrorPrefix,
                emailPrefix: envData.emailPrefix,
                smsPrefix: envData.smsPrefix,
                pushPrefix: envData.pushPrefix,
                emailMasivPrefix: envData.emailMasivPrefix,
                smsMasivPrefix: smsMasivPrefix.smsMasivPrefix
            }
        },
    }
}
