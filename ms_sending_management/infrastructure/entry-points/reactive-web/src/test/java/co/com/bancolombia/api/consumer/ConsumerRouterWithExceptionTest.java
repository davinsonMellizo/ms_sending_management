package co.com.bancolombia.api.consumer;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.consumer.ConsumerHandler;
import co.com.bancolombia.api.services.consumer.ConsumerRouter;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.config.model.consumer.Consumer;
import co.com.bancolombia.usecase.consumer.ConsumerUseCase;
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

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CONSUMER_NOT_FOUND;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ConsumerRouter.class,
        ConsumerHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class ConsumerRouterWithExceptionTest extends BaseIntegration {

    @MockBean
    private ConsumerUseCase useCase;
    private String request;
    private final Consumer consumer = new Consumer();
    private final static String ID = "/{id}";

    @BeforeEach
    public void init() {
        request = loadFileConfig("ConsumerRequest.json", String.class);
    }

    @Test
    public void saveConsumerWithException() {
        when(useCase.saveConsumer(any())).thenReturn(Mono.error(new TechnicalException(INTERNAL_SERVER_ERROR)));
        statusAssertionsWebClientPost(properties.getConsumer(),
                request)
                .is5xxServerError();
        verify(useCase).saveConsumer(any());
    }

    @Test
    public void updateConsumerWithException() {
        when(useCase.updateConsumer(any())).thenReturn(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)));
        JsonNode response = statusAssertionsWebClientPut(properties.getConsumer(),
                request)
                .is5xxServerError()
                .expectBody(JsonNode.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(CONSUMER_NOT_FOUND.getCode(), response.get("code").textValue());
    }

    @Test
    public void saveConsumerWithExceptionTechnical() {
        when(useCase.updateConsumer(any())).thenReturn(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)));
        JsonNode response = statusAssertionsWebClientPut(properties.getConsumer(),
                "{}")
                .is5xxServerError()
                .expectBody(JsonNode.class)
                .returnResult()
                .getResponseBody();
        Assertions.assertEquals(BODY_MISSING_ERROR.getCode(), response.get("code").textValue());
    }

    @Test
    public void deleteConsumerWithException() {
        when(useCase.deleteConsumerById(any())).thenReturn(Mono.error(new Exception()));
        final WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getConsumer() + ID, "ALT")
                .exchange();
        spec.expectStatus().is5xxServerError();
    }
}
