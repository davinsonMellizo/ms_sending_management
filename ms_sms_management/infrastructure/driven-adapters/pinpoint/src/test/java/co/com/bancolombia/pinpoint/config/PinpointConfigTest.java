package co.com.bancolombia.pinpoint.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class PinpointConfigTest {

    @InjectMocks
    private PinpointConfig pinpointConfig;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void sesConfigLocalTest(){
        assertThat(pinpointConfig.pinpointConfigLocal()).isNotNull();
    }

    @Test
    public void sesConfigTest(){
        assertThat(pinpointConfig.pinpointConfig()).isNotNull();
    }

}
