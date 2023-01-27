package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamo.config.DynamoDBTablesProperties;
import co.com.bancolombia.dynamodb.data.SecretData;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.DynamoGateway;
import co.com.bancolombia.model.token.Secret;
import co.com.bancolombia.model.token.SecretGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class TokenAdapter extends AdapterOperations<Secret, SecretData> implements SecretGateway, DynamoGateway {
    @Autowired
    private  SyncSecretVault secretsManager;

    public TokenAdapter(DynamoDbEnhancedAsyncClient client, final DynamoDBTablesProperties dynamoDBTablesProperties)  {
        super(client, dynamoDBTablesProperties);
    }


    @Override
    public Mono<Account> getSecretName(String priorityProvider) {
        return findById(priorityProvider)
                .switchIfEmpty(Mono.error(new Throwable("Not secret Name in dynamodb")))
                .flatMap(secret -> Mono.just(secretsManager.getSecret(secret.getSecretName(), Account.class)));
    }

    @Override
    public Mono<Secret> getTokenName(String priorityProvider) {
        return findById(priorityProvider);
    }
}
