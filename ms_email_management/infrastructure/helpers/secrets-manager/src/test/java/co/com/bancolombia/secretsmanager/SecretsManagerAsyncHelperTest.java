package co.com.bancolombia.secretsmanager;

import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;

public class SecretsManagerAsyncHelperTest {

    @Mock
    private SecretsHelper connector;
    

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldGetSecret() throws SecretException {
        assertThat(connector.createConfigFromSecret(any(), any())).isNull();
    }

}