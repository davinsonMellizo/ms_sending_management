package co.com.bancolombia.ses.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class SesConfigTest {

    @InjectMocks
    private SesConfig sesConfig;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void sesConfigLocalTest(){
        assertThat(sesConfig.sesConfigLocal()).isNotNull();
    }

    @Test
    public void sesConfigTest(){
        assertThat(sesConfig.sesConfig()).isNotNull();
    }
}
