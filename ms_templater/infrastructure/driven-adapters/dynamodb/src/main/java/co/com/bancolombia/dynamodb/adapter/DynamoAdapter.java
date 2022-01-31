package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamodb.data.Templater;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class DynamoAdapter extends AdapterOperations<TemplateRequest, Templater> implements TemplateRepository {

    public DynamoAdapter(final DynamoDbEnhancedAsyncClient client) {
        super(client);
    }

    private static final Logger LOGGER = LogManager.getLogger(DynamoAdapter.class);

    @Override
    public Mono<TemplateRequest> getTemplate(String idTemplate) {
        return findById(idTemplate);
        //.map(templateRequest -> TemplateRequest.builder().idTemplate(templateRequest.getIdTemplate()).build());
    }

    @Override
    public Mono<TemplateRequest> createTemplate(TemplateRequest templateRequest) {
        return save(templateRequest)
                .doOnSuccess(e -> LOGGER.info("Plantilla creada"))
                .flatMap(unused -> this.getTemplate(templateRequest.getIdTemplate()));
    }

    @Override
    public Mono<TemplateRequest> updateTemplate(TemplateRequest templateRequest) {
        return update(templateRequest);
    }


}
