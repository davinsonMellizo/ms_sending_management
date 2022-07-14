package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamodb.data.Templater;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class DynamoAdapter extends AdapterOperations<TemplateRequest, Templater> implements TemplateRepository {

    public DynamoAdapter(final DynamoDbEnhancedAsyncClient client) {
        super(client);
    }

    @Override
    public Mono<TemplateResponse> createTemplate(TemplateRequest templateRequest) {
        return save(templateRequest.toBuilder().idTemplate(null).build())
                .then(findById(templateRequest.getIdTemplate()))
                .map(response -> TemplateResponse.builder()
                        .idTemplate(response.getIdTemplate())
                        .messageType(response.getMessageType())
                        .version(response.getVersion())
                        .idConsumer(response.getIdConsumer())
                        .description(response.getDescription())
                        .messageSubject(response.getMessageSubject())
                        .messageBody(response.getMessageBody())
                        .plainText(response.getPlainText())
                        .creationUser(response.getCreationUser())
                        .creationDate(response.getCreationDate())
                        .modificationUser(response.getModificationUser())
                        .modificationDate(response.getModificationDate())
                        .build());
    }

    @Override
    public Mono<TemplateResponse> getTemplate(String idTemplate) {
        return findById(idTemplate)
                .map(templateRequest -> TemplateResponse.builder()
                        .idTemplate(templateRequest.getIdTemplate())
                        .messageType(templateRequest.getMessageType())
                        .version(templateRequest.getVersion())
                        .idConsumer(templateRequest.getIdConsumer())
                        .description(templateRequest.getDescription())
                        .messageSubject(templateRequest.getMessageSubject())
                        .messageBody(templateRequest.getMessageBody())
                        .plainText(templateRequest.getPlainText())
                        .creationUser(templateRequest.getCreationUser())
                        .creationDate(templateRequest.getCreationDate())
                        .modificationUser(templateRequest.getModificationUser())
                        .modificationDate(templateRequest.getModificationDate())
                        .build());
    }

    @Override
    public Mono<TemplateResponse> updateTemplate(TemplateRequest templateRequest) {
        return update(templateRequest)
                .map(response -> TemplateResponse.builder()
                        .idTemplate(response.getIdTemplate())
                        .messageType(response.getMessageType())
                        .version(response.getVersion())
                        .idConsumer(response.getIdConsumer())
                        .description(response.getDescription())
                        .messageSubject(response.getMessageSubject())
                        .messageBody(response.getMessageBody())
                        .plainText(response.getPlainText())
                        .creationUser(response.getCreationUser())
                        .creationDate(response.getCreationDate())
                        .modificationUser(response.getModificationUser())
                        .modificationDate(response.getModificationDate())
                        .build());
    }

    @Override
    public Mono<TemplateRequest> deleteTemplate(TemplateRequest templateRequest) {
        return delete(templateRequest.getIdTemplate())
                .map(unused -> TemplateRequest.builder().build());
    }
}
