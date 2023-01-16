package co.com.bancolombia.glue.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.glue.GlueAsyncClient;

import static org.assertj.core.api.Assertions.assertThat;

class GlueConfigTest {

    @InjectMocks
    private GlueConfig glueConfig;

    @Mock
    private GlueAsyncClient glueAsyncClient;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void glueConfigSuccess() {
        assertThat(glueAsyncClient).isNotNull();
        assertThat(glueConfig.glueClientConfig()).isNotNull();
    }
}
