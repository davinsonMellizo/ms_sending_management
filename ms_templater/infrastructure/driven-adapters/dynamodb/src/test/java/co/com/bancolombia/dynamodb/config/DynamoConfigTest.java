package co.com.bancolombia.dynamodb.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class DynamoConfigTest {

    @InjectMocks
    private DynamoConfig dynamoConfig;


    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @Mock
    private Properties properties;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        dynamoDbAsyncClient = Mockito.mock(DynamoDbAsyncClient.class);
        when(properties.getEndpoint()).thenReturn("http://localhost:4566");
        when(properties.getRegionAws()).thenReturn("us-east-1");
    }

    @Test
    void dynamoConfigLocalTest(){
        assertThat(dynamoConfig.clientLocal()).isNotNull();
    }

    @Test
    void dynamoConfigEnhancedTest(){
        assertThat(dynamoConfig.clientEnhanced(dynamoDbAsyncClient)).isNotNull();
    }

    @Test
    void dynamoConfigTest(){
        assertThat(dynamoConfig.clientEnvironments()).isNotNull();
    }
}
