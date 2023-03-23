Feature: Data to the SMS format of the Masivian provider

  Scenario: Validate output records in Masivian SMS format
    * def csvValidateFields =
    """
    function fn() {
      const AwsProperties = Java.type('karate.utils.aws.AwsProperties');
      const S3Client = Java.type('karate.utils.aws.s3.S3Client');
      const SmsFormatMasiv = Java.type('karate.sms_format_masiv.SmsFormatMasiv')
      const awsProperties = AwsProperties.fromMap(karate.get('aws'));
      var s3client = new S3Client(awsProperties);
      var smsFormatMasiv = new SmsFormatMasiv(s3client);
      return smsFormatMasiv.validateSmsFormat();
    }
    """
    * def validateSmsMasivFormat = call csvValidateFields
    * print validateSmsMasivFormat
    And match validateSmsMasivFormat == true