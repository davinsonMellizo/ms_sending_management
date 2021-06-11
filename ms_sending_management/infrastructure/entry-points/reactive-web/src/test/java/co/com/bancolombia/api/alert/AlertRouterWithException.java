package co.com.bancolombia.api.alert;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.alert.AlertHandler;
import co.com.bancolombia.api.services.alert.AlertRouter;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.usecase.alert.AlertUseCase;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;
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
public class AlertRouterWithException extends BaseIntegration {

    @MockBean
    private AlertUseCase useCase;
    private String request;
    private final Alert alert = new Alert();

    @BeforeEach
    public void init() {
        request = loadFileConfig("AlertRequest.json", String.class);
    }

    @Test
    public void saveAlertWithException() {
        when(useCase.saveAlertRequest(any())).thenReturn(Mono.error(new TechnicalException(INTERNAL_SERVER_ERROR)));
        statusAssertionsWebClientPost(properties.getSaveAlert(),
                request)
                .is5xxServerError();
        verify(useCase).saveAlertRequest(any());
    }

    @Test
    public void updateAlertsWithException() {
        when(useCase.updateAlertRequest(any())).thenReturn(Mono.error(new BusinessException(ALERT_NOT_FOUND)));
        JsonNode response = statusAssertionsWebClientPut(properties.getUpdateAlert(),
                request)
                .is5xxServerError()
                .expectBody(JsonNode.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(ALERT_NOT_FOUND.getCode(), response.get("code").textValue());
    }

    @Test
    public void saveAlertWithExceptionTechnical() {
        when(useCase.updateAlertRequest(any())).thenReturn(Mono.error(new BusinessException(ALERT_NOT_FOUND)));
        JsonNode response = statusAssertionsWebClientPut(properties.getUpdateAlert(),
                "{}")
                .is5xxServerError()
                .expectBody(JsonNode.class)
                .returnResult()
                .getResponseBody();
        Assertions.assertEquals(BODY_MISSING_ERROR.getCode(), response.get("code").textValue());
    }

    @Test
    public void deleteAlertsWithException() {
        when(useCase.deleteAlertRequest(any())).thenReturn(Mono.error(new Exception()));
        final WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getDeleteAlert() + "?id=ALT")
                .exchange();
        spec.expectStatus().is5xxServerError();
    }

    @Test
    public void deleteAlertsWithExceptionHeaders() {
        final WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getDeleteAlert())
                .exchange();
        spec.expectStatus().is5xxServerError();
    }
}
