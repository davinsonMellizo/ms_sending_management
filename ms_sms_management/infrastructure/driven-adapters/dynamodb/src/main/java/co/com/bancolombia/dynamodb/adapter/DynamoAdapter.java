package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamodb.data.SecretData;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.Secret;
import co.com.bancolombia.model.token.SecretGateway;
import co.com.bancolombia.secretsmanager.SecretsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class DynamoAdapter extends AdapterOperations<Secret, SecretData> implements SecretGateway {

    @Autowired
    private SecretsManager secretsManager;

    public DynamoAdapter(final DynamoDbEnhancedAsyncClient client) {
        super(client);
    }

    @Override
    public Mono<Account> getSecretName(String priorityProvider){
        return findById(priorityProvider)
                .flatMap(secret -> secretsManager.getSecret(secret.getSecretName(), Account.class));


    }

}
