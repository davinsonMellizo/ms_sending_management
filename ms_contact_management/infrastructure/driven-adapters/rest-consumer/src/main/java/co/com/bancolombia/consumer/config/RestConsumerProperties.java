package co.com.bancolombia.consumer.config;

import co.com.bancolombia.d2b.webclient.D2BWebClientFactory;
import co.com.bancolombia.d2b.webclient.model.WebClientRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.HashMap;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class RestConsumerProperties {

    @Bean(name = "webClient")
    public WebClient webClientConfig(D2BWebClientFactory d2bWebClientFactory,
                                     ExchangeFilterFunction apicCredentialsExchangeFunction) {
        var header = new HashMap<String, String>();
        header.put(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        header.put(ACCEPT, APPLICATION_JSON_VALUE);
        return d2bWebClientFactory.createWebClientFor(
                WebClientRequest.builder()
                        .defaultStaticHeaders(header)
                        .exchangeFilterFunction(apicCredentialsExchangeFunction)
                        .build()
        );
    }

    private ClientHttpConnector clientHttpConnector(int timeout) {
        return new ReactorClientHttpConnector(HttpClient.create().responseTimeout(Duration.ofMillis(timeout)));
    }

    @Bean(name = "webClientInternal")
    public WebClient webClientConfigInternal(ConsumerProperties consumerProperties) {
        return WebClient.builder()
                .clientConnector(clientHttpConnector(consumerProperties.getTimeout()))
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .build();
    }

}
