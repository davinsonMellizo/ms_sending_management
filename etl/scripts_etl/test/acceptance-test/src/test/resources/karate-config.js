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
                bucketNameSource: 'nu0154001-alertas-dev-glue-source-data',
                bucketNameTarget: 'nu0154001-alertas-dev-glue-processed-data',
                massivePrefix: 'test',
                massiveErrorPrefix: 'test/error/formato_masivos',
                emailPrefix: 'email/test/formato_masivos',
                smsPrefix: 'sms/test/formato_masivos',
                pushPrefix: 'push/test/formato_masivos'
            }
        },
    }
}
