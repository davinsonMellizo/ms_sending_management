package co.com.bancolombia.dynamodb.data;

import co.com.bancolombia.dynamo.annotation.DynamoDbTableAdapter;
import co.com.bancolombia.dynamo.config.SourceName;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
@DynamoDbTableAdapter(tableName = SourceName.TEMPLATEEMAIL)
public class TemplateEmailData {
    private String name;
    private String subject;
    private String bodyHtml;

    @DynamoDbPartitionKey
    public String getName() {
        return name;
    }
}
