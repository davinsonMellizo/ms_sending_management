Feature: Generate central repository Acceptance test

  Scenario: Validate number of processed EMAIL and SMS records
    * def csvValidateFields =
    """
    function fn() {
      const AwsProperties = Java.type('karate.infrastructure.aws.AwsProperties');
      const S3Client = Java.type('karate.infrastructure.aws.s3.S3Client');
      const awsProperties = AwsProperties.fromMap(karate.get('aws'));
      var s3client = new S3Client(awsProperties);
      return s3client.validateContactsFlow();
    }
    """
    * def validateProcessedBucket = call csvValidateFields
    * print validateProcessedBucket
    And match validateProcessedBucket == true
