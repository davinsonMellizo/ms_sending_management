package co.com.bancolombia.usecase.provider;


import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.config.model.provider.Provider;
import co.com.bancolombia.config.model.provider.gateways.ProviderGateway;
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
public class ProviderUseCaseTest {

    @InjectMocks
    private ProviderUseCase useCase;

    @Mock
    private ProviderGateway providerGateway;

    private final Provider provider = new Provider();

    @BeforeEach
    private void init() {
        provider.setId("ALT");
    }

    @Test
    public void findProviderById() {
        when(providerGateway.findProviderById(anyString()))
                .thenReturn(Mono.just(provider));
        StepVerifier
                .create(useCase.findProviderById(provider.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(providerGateway).findProviderById(anyString());
    }

    @Test
    public void findAllProvider() {
        when(providerGateway.findAll())
                .thenReturn(Mono.just(List.of(provider)));
        StepVerifier
                .create(useCase.findAllProviders())
                .consumeNextWith(providers -> assertEquals(1, providers.size()))
                .verifyComplete();
        verify(providerGateway).findAll();
    }

    @Test
    public void saveProvider() {
        when(providerGateway.saveProvider(any()))
                .thenReturn(Mono.just(provider));
        StepVerifier
                .create(useCase.saveProvider(provider))
                .assertNext(response -> response
                        .getId().equals(provider.getId()))
                .verifyComplete();
        verify(providerGateway).saveProvider(any());
    }

    @Test
    public void updateProvider() {
        when(providerGateway.updateProvider(any()))
                .thenReturn(Mono.just(StatusResponse.<Provider>builder().actual(provider).before(provider).build()));
        StepVerifier
                .create(useCase.updateProvider(provider))
                .assertNext(response -> response
                        .getActual().getId()
                        .equals(provider.getId()))
                .verifyComplete();
        verify(providerGateway).updateProvider(any());
    }

    @Test
    public void deleteProvider() {
        when(providerGateway.findProviderById(anyString()))
                .thenReturn(Mono.just(provider));
        when(providerGateway.deleteProviderById(any()))
                .thenReturn(Mono.just(provider.getId()));
        StepVerifier.create(useCase.deleteProviderById(provider.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(providerGateway).deleteProviderById(any());
    }

    @Test
    public void updateProviderWithException() {
        when(providerGateway.updateProvider(any()))
                .thenReturn(Mono.empty());
        useCase.updateProvider(provider)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void deleteProviderWithException() {
        when(providerGateway.findProviderById(anyString()))
                .thenReturn(Mono.empty());
        useCase.deleteProviderById(provider.getId())
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}
