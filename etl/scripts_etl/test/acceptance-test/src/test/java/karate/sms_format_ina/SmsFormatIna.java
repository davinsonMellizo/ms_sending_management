package karate.sms_format_ina;

import karate.utils.aws.s3.S3Client;
import karate.utils.aws.s3.S3Operations;
import karate.utils.spark.Spark;

public class SmsFormatIna {
    private final S3Client s3Client;
    private final Spark spark;
    private static final int TOTAL_RECORDS = 10;

    public SmsFormatIna(S3Client s3Client) {
        this.s3Client = s3Client;
        this.spark = new Spark(new S3Operations(s3Client));
    }

    public boolean validateSmsFormat() {
        return true;
    }
}
