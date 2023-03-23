package karate.sms_format_masiv;

import com.google.common.collect.Multimap;
import karate.utils.aws.s3.S3Client;
import karate.utils.aws.s3.S3Operations;
import karate.utils.spark.Spark;

import java.util.Arrays;
import java.util.Map;

public class SmsFormatMasiv {

    private final S3Client s3Client;
    private final Spark spark;
    private static final int TOTAL_RECORDS = 4;
    private static final String[] FILE_COLUMNS = {"numero", "mensaje"};

    public SmsFormatMasiv(S3Client s3Client) {
        this.s3Client = s3Client;
        this.spark = new Spark(new S3Operations(s3Client));
    }

    public boolean validateSmsFormat() {
        Multimap<String, Map<String, Object>> smsCsv = spark.readFileFromBucket(
                s3Client.getProperties().getBucketProcessedMasiv(), s3Client.getProperties().getSmsMasivPrefix()
        );
        long smsCsvCount = smsCsv.get("DATA").size();
        boolean checkTotalRecords = (smsCsvCount == TOTAL_RECORDS);
        boolean checkColumns = Arrays.equals(spark.getFileColumns(), FILE_COLUMNS);
        return checkTotalRecords && checkColumns;
    }
}
