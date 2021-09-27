package co.com.bancolombia.usecase.consumer;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.config.model.consumer.Consumer;
import co.com.bancolombia.config.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.config.model.response.StatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsumerUseCaseTest {

    @InjectMocks
    private ConsumerUseCase useCase;

    @Mock
    private ConsumerGateway consumerGateway;

    private final Consumer consumer = new Consumer();

    @BeforeEach
    private void init() {
        consumer.setId("1");
    }

    @Test
    public void findConsumerById() {
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(consumer));
        StepVerifier
                .create(useCase.findConsumerById(consumer.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(consumerGateway).findConsumerById(anyString());
    }

    @Test
    public void findAllConsumers() {
        when(consumerGateway.findAll())
                .thenReturn(Mono.just(List.of(consumer)));
        StepVerifier
                .create(useCase.findAllConsumer())
                .assertNext(consumers -> assertEquals(1, consumers.size()))
                .verifyComplete();
        verify(consumerGateway).findAll();
    }

    @Test
    public void saveConsumer() {
        when(consumerGateway.saveConsumer(any()))
                .thenReturn(Mono.just(consumer));
        StepVerifier
                .create(useCase.saveConsumer(consumer))
                .assertNext(response -> response
                        .getId().equals(consumer.getId()))
                .verifyComplete();
        verify(consumerGateway).saveConsumer(any());
    }

    @Test
    public void updateConsumer() {
        when(consumerGateway.updateConsumer(any()))
                .thenReturn(Mono.just(StatusResponse.<Consumer>builder()
                        .actual(consumer).before(consumer).build()));
        StepVerifier
                .create(useCase.updateConsumer(consumer))
                .assertNext(response -> response
                        .getActual().getId()
                        .equals(consumer.getId()))
                .verifyComplete();
        verify(consumerGateway).updateConsumer(any());
    }

    @Test
    public void deleteConsumer() {
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.just(consumer));
        when(consumerGateway.deleteConsumerById(any()))
                .thenReturn(Mono.just(consumer.getId()));
        StepVerifier.create(useCase.deleteConsumerById(consumer.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(consumerGateway).deleteConsumerById(any());
    }

    @Test
    public void updateConsumerWithException() {
        when(consumerGateway.updateConsumer(any()))
                .thenReturn(Mono.empty());
        useCase.updateConsumer(consumer)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void deleteConsumerWithException() {
        when(consumerGateway.findConsumerById(anyString()))
                .thenReturn(Mono.empty());
        useCase.deleteConsumerById(consumer.getId())
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}
