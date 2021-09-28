package co.com.bancolombia.api.consumer;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.consumer.ConsumerHandler;
import co.com.bancolombia.api.services.consumer.ConsumerRouter;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.consumer.ConsumerUseCase;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ConsumerRouter.class,
        ConsumerHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class ConsumerRouterTest extends BaseIntegration {

    @MockBean
    private ConsumerUseCase useCase;
    private String request;
    private final Consumer consumer = new Consumer();
    private final static String ID = "/{id}";

    @BeforeEach
    public void init() {
        request = loadFileConfig("ConsumerRequest.json", String.class);
    }

    @Test
    public void findConsumerById() {
        when(useCase.findConsumerById(anyString())).thenReturn(Mono.just(consumer));
        webTestClient.get().uri(properties.getConsumer() + ID, "0")
                .exchange()
                .expectStatus()
                .isOk();
        verify(useCase).findConsumerById(anyString());
    }

    @Test
    public void findAllConsumers() {
        when(useCase.findAllConsumer()).thenReturn(Mono.just(List.of(consumer)));
        webTestClient.get().uri(properties.getConsumer())
                .exchange()
                .expectStatus()
                .isOk();
        verify(useCase).findAllConsumer();
    }

    @Test
    public void saveConsumer() {
        when(useCase.saveConsumer(any())).thenReturn(Mono.just(consumer));
        statusAssertionsWebClientPost(properties.getConsumer(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveConsumer(any());
    }

    @Test
    public void updateConsumer() {
        when(useCase.updateConsumer(any())).thenReturn(Mono.just(StatusResponse.<Consumer>builder()
                .before(consumer)
                .actual(consumer)
                .build()));
        statusAssertionsWebClientPut(properties.getConsumer(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updateConsumer(any());
    }

    @Test
    public void deleteConsumer() {
        when(useCase.deleteConsumerById(anyString())).thenReturn(Mono.just("1"));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getConsumer() + ID, "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteConsumerById(anyString());
    }
}
