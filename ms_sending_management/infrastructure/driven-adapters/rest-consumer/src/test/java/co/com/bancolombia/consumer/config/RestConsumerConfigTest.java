package co.com.bancolombia.consumer.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class RestConsumerConfigTest {
    @InjectMocks
    private RestConsumerConfig restConsumerConfig;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void sesConfigTest(){
        assertThat(restConsumerConfig.webClientConfig(new ConsumerProperties(3600, null))).isNotNull();
    }
}
