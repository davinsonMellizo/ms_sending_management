package co.com.bancolombia.dynamodb.data;

import co.com.bancolombia.dynamo.annotation.DynamoDbTableAdapter;
import co.com.bancolombia.dynamo.config.SourceName;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
@DynamoDbTableAdapter(tableName = SourceName.SECRETPRIORITY)
public class SecretData {
    private String secretName;
    private String priorityProvider;

    @DynamoDbPartitionKey
    public String getPriorityProvider() {
        return priorityProvider;
    }
}
