package co.com.bancolombia.usecase.providerservice;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.providerservice.ProviderService;
import co.com.bancolombia.model.providerservice.gateways.ProviderServiceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProviderServiceUseCaseTest {

    @InjectMocks
    private ProviderServiceUseCase useCase;

    @Mock
    private ProviderServiceGateway providerServiceGateway;

    private final ProviderService providerService = new ProviderService();

    @BeforeEach
    private void init() {
        providerService.setIdProvider("TOD");
    }

    @Test
    public void findAllProviderService() {
        when(providerServiceGateway.findAllProviderService())
                .thenReturn(Flux.just(providerService));
        StepVerifier
                .create(useCase.findAllProviderService())
                .expectNextCount(1)
                .verifyComplete();
        verify(providerServiceGateway).findAllProviderService();
    }

    @Test
    public void saveProviderService() {
        when(providerServiceGateway.saveProviderService(any()))
                .thenReturn(Mono.just(providerService));
        StepVerifier
                .create(useCase.saveProviderService(providerService))
                .assertNext(response -> response
                        .getIdProvider().equals(providerService.getIdProvider()))
                .verifyComplete();
        verify(providerServiceGateway).saveProviderService(any());
    }

    @Test
    public void deleteProviderService() {
        when(providerServiceGateway.deleteProviderService(any()))
                .thenReturn(Mono.just(1));
        StepVerifier.create(useCase.deleteProviderService(providerService))
                .expectNextCount(1)
                .verifyComplete();
        verify(providerServiceGateway).deleteProviderService(any());
    }

    @Test
    public void deleteProviderServiceWithException() {
        when(providerServiceGateway.deleteProviderService(any()))
                .thenReturn(Mono.empty());
        useCase.deleteProviderService(providerService)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}
