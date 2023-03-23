package karate.email_format_masiv;

import com.google.common.collect.Multimap;
import karate.utils.aws.s3.S3Client;
import karate.utils.aws.s3.S3Operations;
import karate.utils.spark.Spark;

import java.util.Arrays;
import java.util.Map;

public class EmailFormatMasiv {

    private final S3Client s3Client;
    private final Spark spark;
    private static final int TOTAL_RECORDS = 10;
    private static final String[] FILE_COLUMNS = {
            "correo", "remitente", "Asunto", "tipo", "plantilla", "C0", "C1", "C2", "C3", "C4"
    };

    public EmailFormatMasiv(S3Client s3Client) {
        this.s3Client = s3Client;
        this.spark = new Spark(new S3Operations(s3Client));
    }

    public boolean validateEmailFormat() {
        Multimap<String, Map<String, Object>> emailCsv = spark.readFileFromBucket(
                s3Client.getProperties().getBucketProcessedMasiv(), s3Client.getProperties().getEmailMasivPrefix()
        );
        long emailCsvCount = emailCsv.get("DATA").size();
        boolean checkTotalRecords = (emailCsvCount == TOTAL_RECORDS);
        boolean checkColumns = Arrays.equals(spark.getFileColumns(), FILE_COLUMNS);
        return checkTotalRecords && checkColumns;
    }
}
