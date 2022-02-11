package co.com.bancolombia.usecase.service;


import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.service.Service;
import co.com.bancolombia.model.service.gateways.ServiceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceUseCaseTest {

    @InjectMocks
    private ServiceUseCase useCase;

    @Mock
    private ServiceGateway serviceGateway;

    private final Service service = new Service();

    @BeforeEach
    public void init() {
        service.setId(0);
    }

    @Test
    public void findServiceById() {
        when(serviceGateway.findServiceById(anyInt()))
                .thenReturn(Mono.just(service));
        StepVerifier
                .create(useCase.findServiceById(service.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(serviceGateway).findServiceById(anyInt());
    }

    @Test
    public void saveService() {
        when(serviceGateway.saveService(any()))
                .thenReturn(Mono.just(service));
        StepVerifier
                .create(useCase.saveService(service))
                .assertNext(response -> response
                        .getId().equals(service.getId()))
                .verifyComplete();
        verify(serviceGateway).saveService(any());
    }

    @Test
    public void updateService() {
        when(serviceGateway.updateService(any()))
                .thenReturn(Mono.just(StatusResponse.<Service>builder().actual(service).before(service).build()));
        StepVerifier
                .create(useCase.updateService(service))
                .assertNext(response -> response
                        .getActual().getId()
                        .equals(service.getId()))
                .verifyComplete();
        verify(serviceGateway).updateService(any());
    }

    @Test
    public void deleteService() {
        when(serviceGateway.findServiceById(anyInt()))
                .thenReturn(Mono.just(service));
        when(serviceGateway.deleteServiceById(any()))
                .thenReturn(Mono.just(service.getId()));
        StepVerifier.create(useCase.deleteServiceById(service.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(serviceGateway).deleteServiceById(any());
    }

    @Test
    public void updateServiceWithException() {
        when(serviceGateway.updateService(any()))
                .thenReturn(Mono.empty());
        useCase.updateService(service)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void deleteServiceWithException() {
        when(serviceGateway.findServiceById(anyInt()))
                .thenReturn(Mono.empty());
        useCase.deleteServiceById(service.getId())
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}
