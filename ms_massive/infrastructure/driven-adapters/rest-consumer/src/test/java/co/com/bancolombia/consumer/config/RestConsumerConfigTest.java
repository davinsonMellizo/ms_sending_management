package co.com.bancolombia.consumer.config;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestConsumerConfigTest {
    @InjectMocks
    private RestConsumerConfig restConsumerConfig;

    @BeforeEach
    public void init() {
          MockitoAnnotations.openMocks(this);
    }

    @Test
    void ConfigMASTest()  {
        Assertions.assertThat(restConsumerConfig.getWebClient()).isNotNull();
    }
}