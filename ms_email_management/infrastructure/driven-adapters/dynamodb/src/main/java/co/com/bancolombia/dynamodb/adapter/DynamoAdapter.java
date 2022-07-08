package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamodb.data.TemplateEmailData;
import co.com.bancolombia.model.message.TemplateEmail;
import co.com.bancolombia.model.message.gateways.TemplateEmailGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class DynamoAdapter extends AdapterOperations<TemplateEmail, TemplateEmailData>
        implements TemplateEmailGateway {


    public DynamoAdapter(final DynamoDbEnhancedAsyncClient client,@Value("${spring.profiles.active}") String profile ) {
        super(client,profile);
    }

    @Override
    public Mono<TemplateEmail> findTemplateEmail(String templateName) {
        //return Mono.just(new TemplateEmail());
        return findById(templateName);
    }
}
