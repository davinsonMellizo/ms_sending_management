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
                bucketNameData:'nu0151001-alert-local-glue-logs-alert',
            }
        },
    }
}
