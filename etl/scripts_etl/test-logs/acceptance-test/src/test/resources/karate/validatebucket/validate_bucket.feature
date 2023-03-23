Feature: Acceptance test for validation of logs on file

  Scenario: Validate logs on file
    * def validateNumberBuckets =
    """
    function fn() {
      const AwsProperties = Java.type('karate.infrastructure.aws.AwsProperties');
      const S3Client = Java.type('karate.infrastructure.aws.s3.S3Client');
      const awsProperties = AwsProperties.fromMap(karate.get('aws'));
      var s3client = new S3Client(awsProperties);
      return s3client.getObject();
    }
    """
    * def validateLogsBucket = call validateNumberBuckets
    * print validateLogsBucket
    And match validateLogsBucket !=  []
