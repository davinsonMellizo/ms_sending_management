package karate.infrastructure.aws.s3;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import karate.infrastructure.aws.AwsProperties;
import karate.infrastructure.aws.S3Properties;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.FileOutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class S3Client {

    private final software.amazon.awssdk.services.s3.S3Client awsS3Client;
    private final S3Properties s3Properties;
    private final SparkSession spark;

    public S3Client(AwsProperties awsProperties) {
        S3ClientBuilder s3ClientBuilder = software.amazon.awssdk.services.s3.S3Client.builder();
        if (awsProperties.hasEndpoint()) {
            s3ClientBuilder = s3ClientBuilder.endpointOverride(URI.create(awsProperties.getEndpoint()));
        }
        awsS3Client = s3ClientBuilder.build();
        s3Properties = awsProperties.getS3();
        spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("spark.master", "local")
                .getOrCreate();
    }

    private Map<String, Object> rowToMap(String[] columns, Row row) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < columns.length; i++) {
            result.put(columns[i], row.get(i));
        }
        return result;
    }

    @SneakyThrows
    private byte[] readAllBytes(ResponseInputStream<GetObjectResponse> objRequest) {
        return IOUtils.toByteArray(objRequest);
    }

    @SneakyThrows
    private String saveFile(String key, String bucketName) {
        return Optional.ofNullable(GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build())
                .map(awsS3Client::getObject)
                .map(this::readAllBytes)
                .map(bytes -> {
                    String fileName = "massive-file";
                    try (FileOutputStream fos = new FileOutputStream(fileName)) {
                        fos.write(bytes);
                    } catch (Exception ignored) {
                    }
                    return fileName;
                }).orElse(null);
    }

    public Multimap<String, Map<String, Object>> readFile(String fileName, Function<Row, String> keyRow) {
        Dataset<Row> csvFileDF = spark.read().option("header", "true").option("delimiter", ";").csv(fileName);
        return csvFileDF.collectAsList().stream()
                .collect(Multimaps.toMultimap(keyRow, v -> rowToMap(csvFileDF.columns(), v), HashMultimap::create));
    }

    private ListObjectsV2Request getListObjectsV2Request(String bucketName, String prefix) {
        return ListObjectsV2Request
                .builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();
    }

    @SneakyThrows
    public Multimap<String, Map<String, Object>> getRecordsFromBucket(String bucketName, String prefix) {
        ListObjectsV2Request listObjects = getListObjectsV2Request(bucketName, prefix);
        Optional<S3Object> optionalS3Object = awsS3Client.listObjectsV2(listObjects).contents().stream()
                .filter(obj -> obj.size() > 0)
                .reduce((first, second) -> second);

        if (optionalS3Object.isPresent()) {
            S3Object s3obj = optionalS3Object.get();

            return Stream.of(s3obj).map(obj -> saveFile(obj.key(), bucketName))
                    .map(fileName -> this.readFile(fileName, k -> "DATA"))
                    .flatMap(l -> l.entries().stream())
                    .collect(Multimaps.toMultimap(Map.Entry::getKey, Map.Entry::getValue, HashMultimap::create));
        }
        return null;
    }

    public boolean validateContactsFlow() {
        Multimap<String, Map<String, Object>> massiveCsv = getRecordsFromBucket(
                s3Properties.getBucketNameSource(), s3Properties.getMassivePrefix());

        Multimap<String, Map<String, Object>> emailCsvProcess = getRecordsFromBucket(
                s3Properties.getBucketNameTarget(), s3Properties.getEmailPrefix());

        Multimap<String, Map<String, Object>> smsCsvProcess = getRecordsFromBucket(
                s3Properties.getBucketNameTarget(), s3Properties.getSmsPrefix());

        Multimap<String, Map<String, Object>> pushCsvProcess = getRecordsFromBucket(
                s3Properties.getBucketNameTarget(), s3Properties.getPushPrefix());

        long channelTypeEmailCount = massiveCsv.get("DATA").stream()
                .filter(stringObjectMap -> stringObjectMap.get("ChannelType").equals("EMAIL"))
                .count();

        long channelTypeSmsCount = massiveCsv.get("DATA").stream()
                .filter(stringObjectMap -> stringObjectMap.get("ChannelType").equals("SMS"))
                .count();

        long channelTypePushCount = massiveCsv.get("DATA").stream()
                .filter(stringObjectMap -> stringObjectMap.get("ChannelType").equals("PUSH"))
                .count();

        return channelTypeEmailCount == emailCsvProcess.size() &&
                channelTypeSmsCount == smsCsvProcess.size() &&
                channelTypePushCount == pushCsvProcess.size();
    }
}
