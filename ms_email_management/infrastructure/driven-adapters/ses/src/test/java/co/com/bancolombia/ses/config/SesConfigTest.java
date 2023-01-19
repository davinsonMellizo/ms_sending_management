package co.com.bancolombia.ses.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class SesConfigTest {

    @InjectMocks
    private SesConfig sesConfig;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sesConfigTest() {
        assertThat(sesConfig.sesClientConfig()).isNotNull();
    }
}
