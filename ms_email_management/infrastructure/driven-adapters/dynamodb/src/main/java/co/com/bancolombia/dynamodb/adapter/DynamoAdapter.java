package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamo.config.DynamoDBTablesProperties;
import co.com.bancolombia.dynamodb.data.TemplateEmailData;
import co.com.bancolombia.model.message.TemplateEmail;
import co.com.bancolombia.model.message.gateways.TemplateEmailGateway;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class DynamoAdapter extends AdapterOperations<TemplateEmail, TemplateEmailData>
        implements TemplateEmailGateway {


    public DynamoAdapter(final DynamoDbEnhancedAsyncClient client, final DynamoDBTablesProperties dynamoDBTablesProperties) {
        super(client,dynamoDBTablesProperties);
    }

    @Override
    public Mono<TemplateEmail> findTemplateEmail(String templateName) {
        return findById(templateName);
    }
}
