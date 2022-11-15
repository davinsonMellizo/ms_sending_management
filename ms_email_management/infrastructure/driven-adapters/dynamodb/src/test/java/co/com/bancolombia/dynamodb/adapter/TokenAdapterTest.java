package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.config.DynamoDBTablesProperties;
import co.com.bancolombia.dynamodb.data.SecretData;
import co.com.bancolombia.model.token.Secret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenAdapterTest {

    private String PRIORITYPROVIDER= "1MAS";
    @Mock
    DynamoDbEnhancedAsyncClient dbEnhancedAsyncClient;
    @Mock
    DynamoDBTablesProperties dynamoDBTablesProperties;
    @Mock
    private  TokenAdapter tokenAdapter;
    @Mock
    private DynamoDbAsyncTable <SecretData> secretDataDynamoDbAsyncTable;

    @BeforeEach
    public void init (){
        MockitoAnnotations.openMocks(this);
        when(dbEnhancedAsyncClient.table(any(), any(BeanTableSchema.class))).thenReturn(secretDataDynamoDbAsyncTable);
        tokenAdapter = new TokenAdapter(dbEnhancedAsyncClient, dynamoDBTablesProperties );

    }
    @Test
    void tokenAdapterTest(){
        assertThat (tokenAdapter).isNotNull();
    }

    @Test
    void getTokenNameTest (){
        when(secretDataDynamoDbAsyncTable.getItem(Key.builder().partitionValue(PRIORITYPROVIDER).build()))
                .thenReturn(CompletableFuture.completedFuture(new SecretData()));
        tokenAdapter.getTokenName(PRIORITYPROVIDER)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

}
