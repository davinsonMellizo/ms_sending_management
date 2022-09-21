package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamodb.data.Templater;
import co.com.bancolombia.model.template.dto.Template;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class DynamoAdapter extends AdapterOperations<Template, Templater> implements TemplateRepository {

    public DynamoAdapter(final DynamoDbEnhancedAsyncClient client) {
        super(client);
    }

    @Override
    public Mono<Template> getTemplate(String idTemplate) {
        return findById(idTemplate);
    }

    @Override
    public Mono<Template> saveTemplate(Template template) {
        return update(template)
                .flatMap(this::templateGenerator);
    }

    @Override
    public Mono<Template> deleteTemplate(Template template) {
        return delete(template.getIdTemplate())
                .map(unused -> Template.builder().build());
    }

    protected Mono<Template> templateGenerator(Templater templater) {
        return Mono.just(Template.builder()
                .idTemplate(templater.getIdTemplate())
                .messageType(templater.getMessageType())
                .version(templater.getVersion())
                .idConsumer(templater.getIdConsumer())
                .description(templater.getDescription())
                .messageSubject(templater.getMessageSubject())
                .messageBody(templater.getMessageBody())
                .plainText(templater.getPlainText())
                .creationUser(templater.getCreationUser())
                .creationDate(templater.getCreationDate())
                .modificationUser(templater.getModificationUser())
                .modificationDate(templater.getModificationDate())
                .status(templater.getStatus())
                .build());
    }
}
