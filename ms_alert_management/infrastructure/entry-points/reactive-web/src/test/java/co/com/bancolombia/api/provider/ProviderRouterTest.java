package co.com.bancolombia.api.provider;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.provider.ProviderHandler;
import co.com.bancolombia.api.services.provider.ProviderRouter;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.provider.ProviderUseCase;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ProviderRouter.class,
        ProviderHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class ProviderRouterTest extends BaseIntegration {

    @MockBean
    private ProviderUseCase useCase;

    @MockBean
    private LoggerBuilder loggerBuilder;

    private String request;
    private final Provider provider = new Provider();
    private final static String ID = "/{id}";

    @BeforeEach
    public void init() {
        request = loadFileConfig("ProviderRequest.json", String.class);
    }

    @Test
    void findProviderById() {
        when(useCase.findProviderById(any())).thenReturn(Mono.just(provider));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(properties.getProvider() + ID, "ALT")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findProviderById(any());
    }

    @Test
    void findAllProviders() {
        when(useCase.findAllProviders()).thenReturn(Mono.just(List.of(provider)));
        webTestClient.get().uri(properties.getProvider())
                .exchange()
                .expectStatus()
                .isOk();
        verify(useCase).findAllProviders();
    }

    @Test
    void saveProvider() {
        when(useCase.saveProvider(any())).thenReturn(Mono.just(provider));
        statusAssertionsWebClientPost(properties.getProvider(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveProvider(any());
    }

    @Test
    void updateProvider() {
        when(useCase.updateProvider(any())).thenReturn(Mono.just(StatusResponse.<Provider>builder()
                .before(provider)
                .actual(provider)
                .build()));
        statusAssertionsWebClientPut(properties.getProvider(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updateProvider(any());
    }

    @Test
    void deleteProvider() {
        when(useCase.deleteProviderById(any())).thenReturn(Mono.just("ALT"));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getProvider() + ID, "ALT")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteProviderById(any());
    }

}
