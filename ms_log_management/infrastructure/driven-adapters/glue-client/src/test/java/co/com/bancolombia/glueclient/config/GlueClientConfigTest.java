package co.com.bancolombia.glueclient.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlueClientConfigTest {

    @Mock
    private AwsGlueProperties awsProperties;
    @InjectMocks
    private GlueClientConfig glueClientConfig;

    @Test
    void getGlueClientLocal() {
        when(awsProperties.getEndpoint()).thenReturn("http://localhost:4566");
        when(awsProperties.getRegion()).thenReturn("us-east-1");
        assertNotNull(glueClientConfig.getGlueClient());
    }
}