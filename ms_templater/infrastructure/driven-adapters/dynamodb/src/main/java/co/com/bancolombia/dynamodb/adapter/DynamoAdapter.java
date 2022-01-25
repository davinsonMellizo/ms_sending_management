package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamodb.data.Templater;
import co.com.bancolombia.model.template.Template;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public abstract class DynamoAdapter extends AdapterOperations<TemplateResponse, Templater> implements TemplateRepository {

    public DynamoAdapter(final DynamoDbEnhancedAsyncClient client) {
        super(client);
    }

    @Override
    public Mono<TemplateResponse> getTemplate(String idTemplate) {
        return findById(idTemplate);
    }

}
