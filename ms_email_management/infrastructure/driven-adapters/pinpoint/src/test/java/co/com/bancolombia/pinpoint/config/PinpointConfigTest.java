package co.com.bancolombia.pinpoint.config;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.CREATE_CLIENT_PINPOINT_ERROR;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
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
