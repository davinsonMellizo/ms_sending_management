package co.com.bancolombia.dynamodb.data;

import ch.qos.logback.classic.db.names.TableName;
import co.com.bancolombia.dynamo.annotation.DynamoDbTableAdapter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
@DynamoDbTableAdapter(tableName = "${project.table-name}")
public class Templater {
    private String IdTemplate;
    private String MessageType;
    @DynamoDbPartitionKey
        public String getId() {
        return IdTemplate;
    }
}