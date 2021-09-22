package co.com.bancolombia.api.providerservice;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.providerservice.ProviderServiceHandler;
import co.com.bancolombia.api.services.providerservice.ProviderServiceRouter;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.providerservice.ProviderService;
import co.com.bancolombia.usecase.providerservice.ProviderServiceUseCase;
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

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ProviderServiceRouter.class,
        ProviderServiceHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class ProviderServiceRouterTest extends BaseIntegration {

    @MockBean
    private ProviderServiceUseCase useCase;
    private String request;
    private final ProviderService providerService = new ProviderService();
    private String url;
    private final static String ID = "/{id}";

    @BeforeEach
    public void init() {
        url = properties.getProvider() + "-service";
        request = loadFileConfig("ProviderServiceRequest.json", String.class);
    }

    @Test
    public void save() {
        when(useCase.saveProviderService(any()))
                .thenReturn(Mono.just(providerService));
        statusAssertionsWebClientPost(url, request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveProviderService(any());
    }

    @Test
    public void findAll() {
        when(useCase.findAllProviderService())
                .thenReturn(Mono.just(List.of(providerService)));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(url)
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findAllProviderService();
    }

    @Test
    public void delete() {
        when(useCase.deleteProviderService(any()))
                .thenReturn(Mono.just(1));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(url)
                .header("id-provider", "1")
                .header("id-service", "123")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteProviderService(any());
    }

    @Test
    public void saveCategoryWithException() {
        when(useCase.saveProviderService(any())).thenReturn(Mono.error(new TechnicalException(INTERNAL_SERVER_ERROR)));
        statusAssertionsWebClientPost(url,
                request)
                .is5xxServerError();
        verify(useCase).saveProviderService(any());
    }
}