package karate.process_contacts;

import com.google.common.collect.Multimap;
import karate.utils.aws.s3.S3Client;
import karate.utils.aws.s3.S3Operations;
import karate.utils.spark.Spark;

import java.util.Map;

public class ProcessContacts {

    private final S3Client s3Client;
    private final Spark spark;
    private static final int HEADER_ROW = 1;
    private static final int ERRORS_PER_CHANNEL = 2;
    private static final int TOTAL_ERRORS = 6;

    public ProcessContacts(S3Client s3Client) {
        this.s3Client = s3Client;
        this.spark = new Spark(new S3Operations(s3Client));
    }

    public boolean validateContactsFlow() {
        Multimap<String, Map<String, Object>> massiveCsv = spark.readFileFromBucket(
                s3Client.getProperties().getBucketNameSource(), s3Client.getProperties().getMassivePrefix()
        );

        Multimap<String, Map<String, Object>> massiveErrorCsv = spark.readFileFromBucket(
                s3Client.getProperties().getBucketNameSource(), s3Client.getProperties().getMassiveErrorPrefix()
        );

        Multimap<String, Map<String, Object>> emailCsvProcess = spark.readFileFromBucket(
                s3Client.getProperties().getBucketNameTarget(), s3Client.getProperties().getEmailPrefix()
        );

        Multimap<String, Map<String, Object>> smsCsvProcess = spark.readFileFromBucket(
                s3Client.getProperties().getBucketNameTarget(), s3Client.getProperties().getSmsPrefix()
        );

        Multimap<String, Map<String, Object>> pushCsvProcess = spark.readFileFromBucket(
                s3Client.getProperties().getBucketNameTarget(), s3Client.getProperties().getPushPrefix()
        );

        long channelTypeEmailCount = massiveCsv.get("DATA").stream()
                .filter(stringObjectMap -> stringObjectMap.get("ChannelType").equals("EMAIL"))
                .count();

        long channelTypeSmsCount = massiveCsv.get("DATA").stream()
                .filter(stringObjectMap -> stringObjectMap.get("ChannelType").equals("SMS"))
                .count();

        long channelTypePushCount = massiveCsv.get("DATA").stream()
                .filter(stringObjectMap -> stringObjectMap.get("ChannelType").equals("PUSH"))
                .count();

        // Check total records per file
        long emailCsvTotal = channelTypeEmailCount - ERRORS_PER_CHANNEL;
        long smsCsvTotal = channelTypeSmsCount - ERRORS_PER_CHANNEL;
        long pushCsvTotal = channelTypePushCount - ERRORS_PER_CHANNEL;

        boolean checkTotalRecords = (emailCsvTotal == emailCsvProcess.size() - HEADER_ROW) &&
                (smsCsvTotal == smsCsvProcess.size() - HEADER_ROW) &&
                (pushCsvTotal == pushCsvProcess.size() - HEADER_ROW);

        // Validate errors count
        long massiveErrorCsvCount = massiveErrorCsv.get("DATA").size();
        boolean checkTotalErrors = (TOTAL_ERRORS == massiveErrorCsvCount);

        // Check output file by channel
        boolean checkRecordsWithHeader = ((emailCsvTotal + HEADER_ROW) == emailCsvProcess.size()) &&
                ((smsCsvTotal + HEADER_ROW) == smsCsvProcess.size()) &&
                ((pushCsvTotal + HEADER_ROW) == pushCsvProcess.size());

        return checkTotalRecords && checkTotalErrors && checkRecordsWithHeader;
    }
}
