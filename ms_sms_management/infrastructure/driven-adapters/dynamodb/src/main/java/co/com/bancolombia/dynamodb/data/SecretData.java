package co.com.bancolombia.dynamodb.data;

import co.com.bancolombia.dynamo.annotation.DynamoDbTableAdapter;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
@DynamoDbTableAdapter(tableName = "alertas-${env}-nu0154001-secret-priority")
public class SecretData {
    private String secretName;
    private String priorityProvider;

    @DynamoDbPartitionKey
    public String getPriorityProvider() {
        return priorityProvider;
    }
}
