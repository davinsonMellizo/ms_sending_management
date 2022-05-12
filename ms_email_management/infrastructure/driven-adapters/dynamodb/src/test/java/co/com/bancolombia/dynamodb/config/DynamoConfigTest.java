package co.com.bancolombia.dynamodb.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class DynamoConfigTest {

    @InjectMocks
    private DynamoConfig dynamoConfig;
    @Mock
    private DynamoDbAsyncClient dynamoDbAsyncClient;
    @Mock
    private Properties properties;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void dynamoConfigLocalTest(){
        when(properties.getEndpoint()).thenReturn("http://localhost:4566");
        when(properties.getRegionAws()).thenReturn("us-east-1");
        assertThat(dynamoConfig.dynamodbConfig()).isNotNull();
    }

    @Test
    public void dynamoConfigTest(){
        assertThat(dynamoConfig.dynamodbConfig()).isNotNull();
    }

   /* @Test
    public void dynamoConfigEnhancedTest(){
        assertThat(dynamoConfig.clientEnhanced(dynamoDbAsyncClient)).isNotNull();
    }*/

}