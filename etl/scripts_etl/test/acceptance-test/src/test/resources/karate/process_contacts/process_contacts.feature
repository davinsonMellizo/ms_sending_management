Feature: Fill in the missing user contact data in the CSV file

  Scenario: Validate number of processed EMAIL, SMS and Push records
    * def csvValidateFields =
    """
    function fn() {
      const AwsProperties = Java.type('karate.utils.aws.AwsProperties');
      const S3Client = Java.type('karate.utils.aws.s3.S3Client');
      const ProcessedContacts = Java.type('karate.process_contacts.ProcessContacts')
      const awsProperties = AwsProperties.fromMap(karate.get('aws'));
      var s3client = new S3Client(awsProperties);
      var processedContacts = new ProcessedContacts(s3client);
      return processedContacts.validateContactsFlow();
    }
    """
    * def validateProcessedBucket = call csvValidateFields
    * print validateProcessedBucket
    And match validateProcessedBucket == true