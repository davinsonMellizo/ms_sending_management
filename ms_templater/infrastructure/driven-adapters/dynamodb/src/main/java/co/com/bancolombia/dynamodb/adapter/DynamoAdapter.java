package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamodb.data.TemplateEmailData;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class DynamoAdapter extends AdapterOperations<TemplateEmailData> {

    public DynamoAdapter(final DynamoDbEnhancedAsyncClient client) {
        super(client);
    }

}
