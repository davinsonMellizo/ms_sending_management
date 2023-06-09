package co.com.bancolombia.usecase.remitter;


import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemitterUseCaseTest {

    @InjectMocks
    private RemitterUseCase useCase;

    @Mock
    private RemitterGateway remitterGateway;

    private final Remitter remitter = new Remitter();

    @BeforeEach
    public void init() {
        remitter.setId(0);
    }

    @Test
    void findRemitterById() {
        when(remitterGateway.findRemitterById(anyInt()))
                .thenReturn(Mono.just(remitter));
        StepVerifier
                .create(useCase.findRemitterById(remitter.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(remitterGateway).findRemitterById(anyInt());
    }

    @Test
    void findAllRemitters() {
        when(remitterGateway.findAll())
                .thenReturn(Mono.just(List.of(remitter)));
        StepVerifier
                .create(useCase.findAllRemitter())
                .assertNext(remitters -> assertEquals(1, remitters.size()))
                .verifyComplete();
        verify(remitterGateway).findAll();
    }

    @Test
    void saveRemitter() {
        when(remitterGateway.saveRemitter(any()))
                .thenReturn(Mono.just(remitter));
        StepVerifier
                .create(useCase.saveRemitter(remitter))
                .assertNext(response -> assertEquals(response.getId(), remitter.getId()))
                .verifyComplete();
        verify(remitterGateway).saveRemitter(any());
    }

    @Test
    void updateRemitter() {
        when(remitterGateway.updateRemitter(any()))
                .thenReturn(Mono.just(StatusResponse.<Remitter>builder().actual(remitter).before(remitter).build()));
        StepVerifier
                .create(useCase.updateRemitter(remitter))
                .assertNext(response -> assertEquals(response.getActual().getId(), remitter.getId()))
                .verifyComplete();
        verify(remitterGateway).updateRemitter(any());
    }

    @Test
    void deleteRemitter() {
        when(remitterGateway.findRemitterById(anyInt()))
                .thenReturn(Mono.just(remitter));
        when(remitterGateway.deleteRemitterById(any()))
                .thenReturn(Mono.just(remitter.getId()));
        StepVerifier.create(useCase.deleteRemitterById(remitter.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(remitterGateway).deleteRemitterById(any());
    }

    @Test
    void updateRemitterWithException() {
        when(remitterGateway.updateRemitter(any()))
                .thenReturn(Mono.empty());
        useCase.updateRemitter(remitter)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void deleteRemitterWithException() {
        when(remitterGateway.findRemitterById(anyInt()))
                .thenReturn(Mono.empty());
        useCase.deleteRemitterById(remitter.getId())
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}
