package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.d2b.model.secret.SyncSecretVault;
import co.com.bancolombia.dynamo.AdapterOperations;
import co.com.bancolombia.dynamo.config.DynamoDBTablesProperties;
import co.com.bancolombia.dynamodb.data.SecretData;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.Secret;
import co.com.bancolombia.model.token.SecretGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.GET_SECRET_NAME_DYNAMO_EXCEPTION;

@Component
public class DynamoAdapter extends AdapterOperations<Secret, SecretData> implements SecretGateway {

    @Autowired
    private  SyncSecretVault secretsManager;

    public DynamoAdapter(final DynamoDbEnhancedAsyncClient client,
                         final DynamoDBTablesProperties dynamoDBTablesProperties) {
        super(client, dynamoDBTablesProperties);
    }

    @Override
    public Mono<Account> getSecretName(String priorityProvider) {
        return findById(priorityProvider)
                .flatMap(secret ->Mono.just( secretsManager.getSecret(secret.getSecretName(), Account.class)))
                .onErrorMap(e -> new TechnicalException(e.getMessage(), GET_SECRET_NAME_DYNAMO_EXCEPTION,1));
    }
}
