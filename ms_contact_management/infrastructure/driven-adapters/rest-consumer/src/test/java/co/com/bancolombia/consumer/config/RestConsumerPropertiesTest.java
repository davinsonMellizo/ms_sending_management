package co.com.bancolombia.consumer.config;

import co.com.bancolombia.d2b.model.secret.AsyncSecretVault;
import co.com.bancolombia.d2b.webclient.D2BWebClientFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import static org.mockito.Mockito.when;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WebFluxTest
@EnableAutoConfiguration
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {AsyncSecretVault.class, D2BWebClientFactory.class,
        ExchangeFilterFunction.class})
public class RestConsumerPropertiesTest {
    @Mock
    private RestConsumerProperties restConsumerConfig;

    @MockBean
    private D2BWebClientFactory d2BWebClientFactory;
    @MockBean
    private ExchangeFilterFunction exchangeFilterFunction;

    @Test
    public void webClientConfig() {
        when(restConsumerConfig.webClientConfig(d2BWebClientFactory, exchangeFilterFunction))
                .thenReturn(WebClient.builder().baseUrl("http://localhost:9095")
                        .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE).clientConnector(
                                new ReactorClientHttpConnector(HttpClient.create().compress(true).keepAlive(true)
                                        .option(CONNECT_TIMEOUT_MILLIS, 5000).doOnConnected(connection -> {
                                            connection.addHandlerLast(new
                                                    ReadTimeoutHandler(5000, MILLISECONDS));
                                            connection.addHandlerLast(new
                                                    WriteTimeoutHandler(5000, MILLISECONDS));
                                        }))
                        ).build());
        assertNotNull(restConsumerConfig.webClientConfig(d2BWebClientFactory,exchangeFilterFunction));
    }
}
