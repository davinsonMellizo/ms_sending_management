package karate.utils.spark;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import karate.utils.aws.s3.S3Operations;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class Spark {

    private final SparkSession spark;
    private final S3Operations s3Operations;
    @Getter
    private String[] fileColumns;

    public Spark(S3Operations s3Operations) {
        this.spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("spark.master", "local")
                .getOrCreate();
        this.s3Operations = s3Operations;
        this.fileColumns = new String[0];
    }

    private Map<String, Object> rowToMap(String[] columns, Row row) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < columns.length; i++) {
            result.put(columns[i], row.get(i));
        }
        return result;
    }

    private Multimap<String, Map<String, Object>> readFile(String fileName, Function<Row, String> keyRow) {
        Dataset<Row> csvFileDF = spark.read().option("header", "true").option("delimiter", ";").csv(fileName);
        this.fileColumns = csvFileDF.columns();
        return csvFileDF.collectAsList().stream()
                .collect(Multimaps.toMultimap(keyRow, v -> rowToMap(this.fileColumns, v), HashMultimap::create));
    }

    @SneakyThrows
    public Multimap<String, Map<String, Object>> readFileFromBucket(String bucketName, String prefix) {
        Optional<S3Object> optionalS3Object = s3Operations.getRecordsFromBucket(bucketName, prefix);
        if (optionalS3Object.isPresent()) {
            S3Object s3obj = optionalS3Object.get();
            return Stream.of(s3obj).map(obj -> s3Operations.saveFile(obj.key(), bucketName))
                    .map(fileName -> this.readFile(fileName, k -> "DATA"))
                    .flatMap(l -> l.entries().stream())
                    .collect(Multimaps.toMultimap(Map.Entry::getKey, Map.Entry::getValue, HashMultimap::create));
        }
        return null;
    }
}
