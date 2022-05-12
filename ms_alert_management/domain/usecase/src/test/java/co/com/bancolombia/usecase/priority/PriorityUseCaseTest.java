package co.com.bancolombia.usecase.priority;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.priority.Priority;
import co.com.bancolombia.model.priority.gateways.PriorityGateway;
import co.com.bancolombia.model.response.StatusResponse;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PriorityUseCaseTest {

    @InjectMocks
    private PriorityUseCase useCase;

    @Mock
    private PriorityGateway priorityGateway;

    private final Priority priority = new Priority();

    @BeforeEach
    private void init() {
        priority.setId(1);
    }

    @Test
    void findCategoryById() {
        when(priorityGateway.findPriorityById(anyInt()))
                .thenReturn(Mono.just(priority));
        StepVerifier
                .create(useCase.findPriorityById(priority.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(priorityGateway).findPriorityById(anyInt());
    }

    @Test
    void findAllPriorities() {
        when(priorityGateway.findAllByProvider(anyString()))
                .thenReturn(Mono.just(List.of(priority)));
        StepVerifier
                .create(useCase.findAllByProvider(anyString()))
                .consumeNextWith(providers -> assertEquals(1, providers.size()))
                .verifyComplete();
        verify(priorityGateway).findAllByProvider(anyString());
    }

    @Test
    void savePriority() {
        when(priorityGateway.savePriority(any()))
                .thenReturn(Mono.just(priority));
        StepVerifier
                .create(useCase.savePriority(priority))
                .assertNext(response -> response
                        .getId().equals(priority.getId()))
                .verifyComplete();
        verify(priorityGateway).savePriority(any());
    }

    @Test
    void updatePriority() {
        when(priorityGateway.updatePriority(any()))
                .thenReturn(Mono.just(StatusResponse.<Priority>builder()
                        .actual(priority).before(priority).build()));
        StepVerifier
                .create(useCase.updatePriority(priority))
                .assertNext(response -> response
                        .getActual().getId()
                        .equals(priority.getId()))
                .verifyComplete();
        verify(priorityGateway).updatePriority(any());
    }

    @Test
    void deletePriority() {
        when(priorityGateway.findPriorityById(anyInt()))
                .thenReturn(Mono.just(priority));
        when(priorityGateway.deletePriorityById(any()))
                .thenReturn(Mono.just(priority.getId()));
        StepVerifier.create(useCase.deletePriorityById(priority.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(priorityGateway).deletePriorityById(any());
    }

    @Test
    void updatePriorityWithException() {
        when(priorityGateway.updatePriority(any()))
                .thenReturn(Mono.empty());
        useCase.updatePriority(priority)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void deletePriorityWithException() {
        when(priorityGateway.findPriorityById(anyInt()))
                .thenReturn(Mono.empty());
        useCase.deletePriorityById(priority.getId())
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}