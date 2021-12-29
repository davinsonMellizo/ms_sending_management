package co.com.bancolombia.api.alerttransaction;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.alerttransaction.AlertTransactionHandler;
import co.com.bancolombia.api.services.alerttransaction.AlertTransactionRouter;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.usecase.alerttransaction.AlertTransactionUseCase;
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
        AlertTransactionRouter.class,
        AlertTransactionHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class AlertTransactionRouterTest extends BaseIntegration {

    @MockBean
    private AlertTransactionUseCase useCase;

    @MockBean
    private LoggerBuilder loggerBuilder;

    private String request;
    private final AlertTransaction alert = new AlertTransaction();
    private final static String ID = "/{id}";
    private String url;

    @BeforeEach
    public void init() {
        url = properties.getAlert()+"-transaction";
        request = loadFileConfig("AlertTransactionRequest.json", String.class);
    }

    @Test
    public void findAllAlertsTransaction() {
        when(useCase.findAllAlertTransaction(any())).thenReturn(Mono.just(List.of(alert)));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(url + ID, "ALT")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findAllAlertTransaction(any());
    }

    @Test
    public void saveAlertTransaction() {
        when(useCase.saveAlertTransaction(any())).thenReturn(Mono.just(alert));
        statusAssertionsWebClientPost(url, request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveAlertTransaction(any());
    }

    @Test
    public void deleteAlertTransaction() {
        when(useCase.deleteAlertTransaction(any())).thenReturn(Mono.just("ALT"));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(url)
                .header("id-alert", "HGD")
                .header("id-transaction", "HGD")
                .header("id-consumer", "HGD")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteAlertTransaction(any());
    }

}
