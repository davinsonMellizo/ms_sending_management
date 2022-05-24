package co.com.bancolombia.dynamodb.data;

import co.com.bancolombia.dynamo.annotation.DynamoDbTableAdapter;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
//@DynamoDbTableAdapter(tableName = "template-email")
@DynamoDbTableAdapter(tableName = "alertas-qa-nu0154001-template-email")
public class TemplateEmailData {
    private String name;
    private String subject;
    private String bodyHtml;

    @DynamoDbPartitionKey
    public String getName() {
        return name;
    }
}
