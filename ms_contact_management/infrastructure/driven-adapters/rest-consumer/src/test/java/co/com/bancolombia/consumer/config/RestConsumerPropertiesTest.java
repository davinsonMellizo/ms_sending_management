package co.com.bancolombia.consumer.config;

import co.com.bancolombia.d2b.webclient.D2BWebClientFactory;
import co.com.bancolombia.d2b.webclient.model.WebClientRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestConsumerPropertiesTest {
    private RestConsumerProperties config;
    private D2BWebClientFactory d2BWebClientFactory;
    private ExchangeFilterFunction exchangeFilterFunction;

    @BeforeEach
    public void init() {
        d2BWebClientFactory = mock(D2BWebClientFactory.class);
        exchangeFilterFunction = mock(ExchangeFilterFunction.class);
    }

    @Test
    void webClientConfig() {
        WebClient webClient = mock(WebClient.class);
        config = new RestConsumerProperties();
        when(d2BWebClientFactory.createWebClientFor(any(WebClientRequest.class))).thenReturn(webClient);
        assertNotNull(config.webClientConfig(d2BWebClientFactory, exchangeFilterFunction));

    }
}
