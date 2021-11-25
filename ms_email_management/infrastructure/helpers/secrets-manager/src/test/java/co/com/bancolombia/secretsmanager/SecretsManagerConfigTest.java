package co.com.bancolombia.secretsmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SecretsManagerConfigTest {

    @InjectMocks
    private SecretsManagerConfig secretsManagerConfig;

    @Mock
    private SecretsManagerProperties properties;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
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
