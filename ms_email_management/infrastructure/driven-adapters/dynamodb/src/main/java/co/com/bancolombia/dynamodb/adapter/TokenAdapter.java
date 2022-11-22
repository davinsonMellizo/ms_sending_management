package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamodb.data.SecretData;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.DynamoGateway;
import co.com.bancolombia.model.token.Secret;
import co.com.bancolombia.model.token.SecretGateway;
import co.com.bancolombia.secretsmanager.SecretsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class TokenAdapter extends AdapterOperations<Secret, SecretData> implements SecretGateway, DynamoGateway {
    @Autowired
    private SecretsManager secretsManager;

    public TokenAdapter(DynamoDbEnhancedAsyncClient client, @Value("${spring.profiles.active}") String profile) {
        super(client, profile);
    }


    @Override
    public Mono<Account> getSecretName(String priorityProvider) {
        return findById(priorityProvider)
                .switchIfEmpty(Mono.error(new Throwable("Not secret Name in dynamodb")))
                .flatMap(secret -> secretsManager.getSecret(secret.getSecretName(), Account.class));
    }

    @Override
    public Mono<Secret> getTokenName(String priorityProvider) {
        return findById(priorityProvider);
    }
}
