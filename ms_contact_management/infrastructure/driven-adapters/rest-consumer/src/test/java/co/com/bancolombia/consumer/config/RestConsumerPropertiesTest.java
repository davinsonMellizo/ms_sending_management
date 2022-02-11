package co.com.bancolombia.consumer.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ConsumerProperties.class
})
public class RestConsumerPropertiesTest {
    private RestConsumerProperties restConsumerConfig = new RestConsumerProperties();
    @Autowired
    private ConsumerProperties consumerProperties;
    @Test
    public void beanClientTokenHuaweiTest() {
        assertNotNull(restConsumerConfig.webClientConfig(consumerProperties));
    }
}
