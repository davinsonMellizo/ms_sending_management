package co.com.bancolombia.dynamodb.data;

import co.com.bancolombia.dynamo.annotation.DynamoDbTableAdapter;
import co.com.bancolombia.dynamo.config.SourceName;
import co.com.bancolombia.model.template.dto.Template;
import lombok.Data;
import lombok.EqualsAndHashCode;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
@DynamoDbTableAdapter(tableName = SourceName.TABLE_NAME)
@EqualsAndHashCode(callSuper=false)
public class Templater extends Template {

    @DynamoDbPartitionKey
    @Override
    public String getIdTemplate() {
        return super.getIdTemplate();

    }
}