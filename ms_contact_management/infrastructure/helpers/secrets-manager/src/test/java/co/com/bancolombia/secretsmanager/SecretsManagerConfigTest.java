package co.com.bancolombia.secretsmanager;

import connector.AWSSecretManagerConnector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SecretsManagerConfigTest {

    @InjectMocks
    private SecretsManagerConfig secretsManagerConfig;

    @Mock
    private AWSSecretManagerConnector awsSecretManagerConnector;

    @Mock
    private SecretsManagerProperties properties;

    @BeforeAll
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(properties.getCacheTime()).thenReturn(3600);
        when(properties.getCacheSize()).thenReturn(300);
    }

    @Test
    public void beanConnectionSecretsManagerTest() {
        assertThat(secretsManagerConfig.connectionAws())
            .isNotNull();
    }

    @Test
    public void beanConnectionLocalSecretsManagerTest() {
        assertThat(secretsManagerConfig.connectionLocal("http://localhost:4566"))
            .isNotNull();
    }

}
