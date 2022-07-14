package co.com.bancolombia.dynamodb.data;

import co.com.bancolombia.dynamo.annotation.DynamoDbTableAdapter;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
@DynamoDbTableAdapter(tableName = "alertas-local-nu0154001-templater-table")
public class Templater {
    private String idTemplate;
    private String messageType;
    private String version;
    private String idConsumer;
    private String description;
    private String messageSubject;
    private String messageBody;
    private String plainText;
    private String creationUser;
    private String creationDate;
    private String modificationUser;
    private String modificationDate;
    private String headers;

    @DynamoDbPartitionKey
    public String getIdTemplate() {
        return idTemplate;
    }
}