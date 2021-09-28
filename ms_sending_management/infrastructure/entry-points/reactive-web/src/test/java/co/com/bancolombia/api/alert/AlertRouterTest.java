package co.com.bancolombia.api.alert;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.alert.AlertHandler;
import co.com.bancolombia.api.services.alert.AlertRouter;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.alert.AlertUseCase;
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
        AlertRouter.class,
        AlertHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class AlertRouterTest extends BaseIntegration {

    @MockBean
    private AlertUseCase useCase;
    private String request;
    private final Alert alert = new Alert();
    private final static String ID = "/{id}";

    @BeforeEach
    public void init() {
        request = loadFileConfig("AlertRequest.json", String.class);
    }

    @Test
    public void findAlertsById() {
        when(useCase.findAlertByIdRequest(any())).thenReturn(Mono.just(alert));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(properties.getAlert() + ID, "ALT")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findAlertByIdRequest(any());
    }

    @Test
    public void saveAlert() {
        when(useCase.saveAlertRequest(any())).thenReturn(Mono.just(alert));
        statusAssertionsWebClientPost(properties.getAlert(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveAlertRequest(any());
    }

    @Test
    public void updateAlert() {
        when(useCase.updateAlertRequest(any())).thenReturn(Mono.just(StatusResponse.<Alert>builder()
                .before(alert)
                .actual(alert)
                .build()));
        statusAssertionsWebClientPut(properties.getAlert(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updateAlertRequest(any());
    }

    @Test
    public void deleteAlert() {
        when(useCase.deleteAlertRequest(any())).thenReturn(Mono.just("ALT"));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getAlert() + ID, "ALT")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteAlertRequest(any());
    }

}
