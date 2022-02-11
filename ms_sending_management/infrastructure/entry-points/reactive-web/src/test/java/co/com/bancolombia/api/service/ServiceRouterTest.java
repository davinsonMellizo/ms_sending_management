package co.com.bancolombia.api.service;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.service.ServiceHandler;
import co.com.bancolombia.api.services.service.ServiceRouter;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.service.Service;
import co.com.bancolombia.usecase.service.ServiceUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ServiceRouter.class,
        ServiceHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class ServiceRouterTest extends BaseIntegration {

    @MockBean
    private ServiceUseCase useCase;

    @MockBean
    private LoggerBuilder loggerBuilder;

    private String request;
    private final Service service = new Service();
    private final static String ID = "/{id}";

    @BeforeEach
    public void init() {
        request = loadFileConfig("ServiceRequest.json", String.class);
    }

    @Test
    public void findServiceById() {
        when(useCase.findServiceById(any())).thenReturn(Mono.just(service));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(properties.getService() + ID, "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findServiceById(any());
    }

    @Test
    public void saveService() {
        when(useCase.saveService(any())).thenReturn(Mono.just(service));
        statusAssertionsWebClientPost(properties.getService(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveService(any());
    }

    @Test
    public void updateService() {
        when(useCase.updateService(any())).thenReturn(Mono.just(StatusResponse.<Service>builder()
                .before(service)
                .actual(service)
                .build()));
        statusAssertionsWebClientPut(properties.getService(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updateService(any());
    }

    @Test
    public void deleteService() {
        when(useCase.deleteServiceById(any())).thenReturn(Mono.just(0));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getService() + ID, "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteServiceById(any());
    }

}
