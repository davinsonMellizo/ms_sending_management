package co.com.bancolombia.dynamodb.adapter;


import co.com.bancolombia.dynamo.config.DynamoDBTablesProperties;
import co.com.bancolombia.dynamodb.data.TemplateEmailData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
public class DynamoAdapterTest {

    private static final String TEMPLATE_NAME ="Compra";

    @Mock
    DynamoDbEnhancedAsyncClient dbEnhancedAsyncClient;
    @Mock
    DynamoDBTablesProperties dynamoDBTablesProperties;
    @Mock
    private DynamoAdapter dynamoAdapter;
    @Mock
    private DynamoDbAsyncTable <TemplateEmailData> dataTable;


    @BeforeEach
    public  void init(){
        MockitoAnnotations.openMocks(this);
        when(dbEnhancedAsyncClient.table(any(), any(BeanTableSchema.class))).thenReturn(dataTable);
        dynamoAdapter = new DynamoAdapter(dbEnhancedAsyncClient, dynamoDBTablesProperties );

    }

    @Test
    void  dynamoAdapterTest (){
        assertThat(dynamoAdapter).isNotNull();
    }

    @Test
    void  findTemplateEmailTest(){
        when(dataTable.getItem(Key.builder().partitionValue(TEMPLATE_NAME).build()))
                .thenReturn(CompletableFuture.completedFuture(new TemplateEmailData()));
        dynamoAdapter.findTemplateEmail(TEMPLATE_NAME)
                        .as(StepVerifier::create)
                                .expectNextCount(1)
                                        .verifyComplete();

 }

}
