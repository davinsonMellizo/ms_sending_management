package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamodb.data.Templater;
import co.com.bancolombia.model.template.Template;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class DynamoAdapter extends AdapterOperations<Template,Templater> {

    public DynamoAdapter(final DynamoDbEnhancedAsyncClient client) {
        super(client);
    }

}
