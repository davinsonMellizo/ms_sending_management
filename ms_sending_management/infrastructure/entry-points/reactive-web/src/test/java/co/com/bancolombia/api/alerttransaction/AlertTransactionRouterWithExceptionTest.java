package co.com.bancolombia.api.alerttransaction;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.alerttransaction.AlertTransactionHandler;
import co.com.bancolombia.api.services.alerttransaction.AlertTransactionRouter;
import co.com.bancolombia.usecase.alerttransaction.AlertTransactionUseCase;
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

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        AlertTransactionRouter.class,
        AlertTransactionHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class AlertTransactionRouterWithExceptionTest extends BaseIntegration {

    @MockBean
    private AlertTransactionUseCase useCase;
    private final static String ID = "/{id}";
    private String url;

    @BeforeEach
    public void init() {
        url = properties.getAlert() + "-transaction";
    }

    @Test
    public void saveAlertTransactionWithExceptionTechnical() {
        JsonNode response = statusAssertionsWebClientPost(url, "{}")
                .is5xxServerError()
                .expectBody(JsonNode.class)
                .returnResult()
                .getResponseBody();
        Assertions.assertEquals(BODY_MISSING_ERROR.getCode(), response.get("code").textValue());
    }

    @Test
    public void deleteAlertTransactionWithException() {
        final WebTestClient.ResponseSpec spec = webTestClient.delete().uri(url)
                .exchange();
        spec.expectStatus().is5xxServerError();
    }
}
