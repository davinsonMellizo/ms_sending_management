package co.com.bancolombia.sqs.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SQSConfigTes {

    @InjectMocks
    private  SQSConfig sqsConfig;

    @Mock
    private SQSProperties properties;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        when(properties.getTimeout()).thenReturn(3000);
        when(properties.getPoolSize()).thenReturn(2);
        when(properties.getUrl()).thenReturn("http://localhost:4566");
        when(properties.getRegionAws()).thenReturn("us-east-1");
    }

    @Test
    public void sqsConfigLocalTest(){
        assertThat(sqsConfig.sqsConfig()).isNotNull();
    }
}
