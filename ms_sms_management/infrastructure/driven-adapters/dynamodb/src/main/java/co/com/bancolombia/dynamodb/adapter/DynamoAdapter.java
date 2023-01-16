package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamo.config.DynamoDBTablesProperties;
import co.com.bancolombia.dynamodb.data.SecretData;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.Secret;
import co.com.bancolombia.model.token.SecretGateway;
import co.com.bancolombia.secretsmanager.SecretsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Component
public class DynamoAdapter extends AdapterOperations<Secret, SecretData> implements SecretGateway {

    @Autowired
    private SecretsManager secretsManager;

    public DynamoAdapter(final DynamoDbEnhancedAsyncClient client,
                         final DynamoDBTablesProperties dynamoDBTablesProperties) {
        super(client, dynamoDBTablesProperties);
    }

    @Override
    public Mono<Account> getSecretName(String priorityProvider) {
        return findById(priorityProvider)
                .flatMap(secret -> secretsManager.getSecret(secret.getSecretName(), Account.class));
    }
}
