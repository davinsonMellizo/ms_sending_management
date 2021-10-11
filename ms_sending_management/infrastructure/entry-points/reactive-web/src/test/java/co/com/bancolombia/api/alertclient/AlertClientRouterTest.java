package co.com.bancolombia.api.alertclient;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.alertclient.AlertClientHandler;
import co.com.bancolombia.api.services.alertclient.AlertClientRouter;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.alertclient.AlertClientUseCase;
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

import static co.com.bancolombia.commons.constants.Header.DOCUMENT_NUMBER;
import static co.com.bancolombia.commons.constants.Header.DOCUMENT_TYPE;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_CLIENT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        AlertClientRouter.class,
        AlertClientHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class AlertClientRouterTest extends BaseIntegration {

    @MockBean
    private AlertClientUseCase useCase;
    private String request;
    private final AlertClient alertClient = new AlertClient();
    private String url;

    @BeforeEach
    public void init() {
        url = properties.getAlert() + "-client";
        request = loadFileConfig("AlertClientRequest.json", String.class);
    }

    @Test
    public void save() {
        when(useCase.saveAlertClient(any()))
                .thenReturn(Mono.just(alertClient));
        statusAssertionsWebClientPost(url, request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveAlertClient(any());
    }

    @Test
    public void findAll() {
        when(useCase.findAllAlertClientByClient(any()))
                .thenReturn(Mono.just(List.of(alertClient)));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(url)
                .header(DOCUMENT_NUMBER, "106177285")
                .header(DOCUMENT_TYPE, "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findAllAlertClientByClient(any());
    }

    @Test
    public void update() {
        when(useCase.updateAlertClient(any())).thenReturn(Mono.just(StatusResponse.<AlertClient>builder()
                .actual(alertClient).before(alertClient).build()));
        statusAssertionsWebClientPut(url,
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updateAlertClient(any());
    }

    @Test
    public void delete() {
        when(useCase.deleteAlertClient(any()))
                .thenReturn(Mono.just("1"));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(url)
                .header("id-alert", "1")
                .header("id-client", "123")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteAlertClient(any());
    }

    @Test
    public void saveAlertClientWithException() {
        when(useCase.saveAlertClient(any())).thenReturn(Mono.error(new TechnicalException(INTERNAL_SERVER_ERROR)));
        statusAssertionsWebClientPost(url,
                request)
                .is5xxServerError();
        verify(useCase).saveAlertClient(any());
    }

    @Test
    public void updateContactsWithException() {
        when(useCase.updateAlertClient(any())).thenReturn(Mono.error(new BusinessException(ALERT_CLIENT_NOT_FOUND)));
        statusAssertionsWebClientPut(url,
                request)
                .is5xxServerError();
    }
}