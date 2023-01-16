package co.com.bancolombia.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class RestConsumerConfig {

    private ClientHttpConnector clientHttpConnector(int timeout) {
        return new ReactorClientHttpConnector(HttpClient.create().responseTimeout(Duration.ofMillis(timeout)));
    }

    @Bean
    public WebClient webClientConfig(final co.com.bancolombia.consumer.config.ConsumerProperties consumerProperties) {
        return WebClient.builder()
                .clientConnector(clientHttpConnector(consumerProperties.getTimeout()))
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .build();
    }

}
