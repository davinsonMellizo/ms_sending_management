package co.com.bancolombia.api.remitter;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.remitter.RemitterHandler;
import co.com.bancolombia.api.services.remitter.RemitterRouter;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.usecase.remitter.RemitterUseCase;
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
        RemitterRouter.class,
        RemitterHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class RemitterRouterWithExceptionTest extends BaseIntegration {

    @MockBean
    private RemitterUseCase useCase;

    @MockBean
    private LoggerBuilder loggerBuilder;

    private String request;
    private final static String ID = "/{id}";

    @BeforeEach
    public void init() {
        request = loadFileConfig("RemitterRequest.json", String.class);
    }

    @Test
    public void saveRemitterWithException() {
        when(useCase.saveRemitter(any())).thenReturn(Mono.error(new TechnicalException(INTERNAL_SERVER_ERROR)));
        statusAssertionsWebClientPost(properties.getRemitter(),
                request)
                .is5xxServerError();
        verify(useCase).saveRemitter(any());
    }

    @Test
    public void updateRemittersWithException() {
        when(useCase.updateRemitter(any())).thenReturn(Mono.error(new BusinessException(ALERT_NOT_FOUND)));
        JsonNode response = statusAssertionsWebClientPut(properties.getRemitter(),
                request)
                .is5xxServerError()
                .expectBody(JsonNode.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(ALERT_NOT_FOUND.getCode(), response.get("code").textValue());
    }

    @Test
    public void saveRemitterWithExceptionTechnical() {
        when(useCase.updateRemitter(any())).thenReturn(Mono.error(new BusinessException(ALERT_NOT_FOUND)));
        JsonNode response = statusAssertionsWebClientPut(properties.getRemitter(),
                "{}")
                .is5xxServerError()
                .expectBody(JsonNode.class)
                .returnResult()
                .getResponseBody();
        Assertions.assertEquals(BODY_MISSING_ERROR.getCode(), response.get("code").textValue());
    }

    @Test
    public void deleteRemittersWithException() {
        when(useCase.deleteRemitterById(any())).thenReturn(Mono.error(new Exception()));
        final WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getRemitter() + ID, "ALT")
                .exchange();
        spec.expectStatus().is5xxServerError();
    }
}
