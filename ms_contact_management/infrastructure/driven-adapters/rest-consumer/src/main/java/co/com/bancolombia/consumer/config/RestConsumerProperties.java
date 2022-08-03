package co.com.bancolombia.consumer.config;

import co.com.bancolombia.d2b.webclient.D2BWebClientFactory;
import co.com.bancolombia.d2b.webclient.model.WebClientRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class RestConsumerProperties {

    @Bean
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

}
