@ignore
Feature: Data to the SMS format of the Inalambria provider

  Scenario: Validate output records in Inalambria SMS format
    * def csvValidateFields =
    """
    function fn() {
      const AwsProperties = Java.type('karate.utils.aws.AwsProperties');
      const S3Client = Java.type('karate.utils.aws.s3.S3Client');
      const SmsFormatIna = Java.type('karate.sms_format_ina.SmsFormatIna')
      const awsProperties = AwsProperties.fromMap(karate.get('aws'));
      var s3client = new S3Client(awsProperties);
      var smsFormatIna = new SmsFormatIna(s3client);
      return smsFormatIna.validateSmsFormat();
    }
    """
    * def validateSmsInaFormat = call csvValidateFields
    * print validateSmsInaFormat
    And match validateSmsInaFormat == true