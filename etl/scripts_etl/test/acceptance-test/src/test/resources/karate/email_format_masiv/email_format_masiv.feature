Feature: Data to the EMAIL format of the Masivian provider

  Scenario: Validate output records in Masivian EMAIL format
    * def csvValidateFields =
    """
    function fn() {
      const AwsProperties = Java.type('karate.utils.aws.AwsProperties');
      const S3Client = Java.type('karate.utils.aws.s3.S3Client');
      const EmailFormatMasiv = Java.type('karate.email_format_masiv.EmailFormatMasiv')
      const awsProperties = AwsProperties.fromMap(karate.get('aws'));
      var s3client = new S3Client(awsProperties);
      var emailFormatMasiv = new EmailFormatMasiv(s3client);
      return emailFormatMasiv.validateEmailFormat();
    }
    """
    * def validateEmailMasivFormat = call csvValidateFields
    * print validateEmailMasivFormat
    And match validateEmailMasivFormat == true