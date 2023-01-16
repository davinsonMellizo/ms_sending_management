package co.com.bancolombia.dynamodb.adapter;

import co.com.bancolombia.dynamo.config.DynamoDBTablesProperties;
import co.com.bancolombia.dynamodb.data.SecretData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DynamodbTest {


    @Mock
    DynamoDbEnhancedAsyncClient dbEnhancedAsyncClient;
    @Mock
    DynamoDBTablesProperties dynamoDBTablesProperties;
    @Mock
    private DynamoAdapter dynamoAdapter;
    @Mock
    private DynamoDbAsyncTable<SecretData> secretDataDynamoDbAsyncTable;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        when(dbEnhancedAsyncClient.table(any(), any(BeanTableSchema.class))).thenReturn(secretDataDynamoDbAsyncTable);
        dynamoAdapter = new DynamoAdapter(dbEnhancedAsyncClient, dynamoDBTablesProperties);

    }

    @Test
    void dynamoAdapter() {
        assertThat(dynamoAdapter).isNotNull();
    }


}
