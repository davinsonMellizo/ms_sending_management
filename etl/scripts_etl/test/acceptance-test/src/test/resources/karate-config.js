function () {
    karate.log('karate.env system property was:', karate.env);
    karate.configure('connectTimeout', 500000);
    karate.configure('readTimeout', 500000);
    karate.configure('ssl', true);

    return {
        aws: {
            endpoint: 'http://localhost:4566',
            region: 'us-east-1',
            s3: {
                bucketNameSource: 'nu0154001-alertas-dev-glue-scripts-data',
                bucketNameTarget: 'nu0154001-alertas-dev-glue-processed-data',
                massivePrefix: 'test',
                emailPrefix: 'test/ChannelType=EMAIL',
                smsPrefix: 'test/ChannelType=SMS',
                pushPrefix: 'test/ChannelType=PUSH'
            }
        },
    }
}
