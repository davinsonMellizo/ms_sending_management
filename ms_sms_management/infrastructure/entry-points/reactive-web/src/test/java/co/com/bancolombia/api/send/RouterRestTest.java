package co.com.bancolombia.api.send;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.Handler;
import co.com.bancolombia.api.RouterRest;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        RouterRest.class,
        Handler.class,
        ApiProperties.class,
        ExceptionHandler.class,
        ValidatorHandler.class
})
public class RouterRestTest extends BaseIntegration {
    @MockBean
    private SendAlertUseCase useCase;
    @MockBean
    private LogUseCase logUseCase;
    @MockBean
    private LoggerBuilder loggerBuilder;
    @Mock
    private ValidatorHandler validatorHandler;


    private String request;

    @BeforeEach
    public void init() {
        request = loadFileConfig("Send.json", String.class);
    }

    @Test
    void findAlertsById() {
        when(useCase.sendAlert(any())).thenReturn(Mono.just(Response.builder().build()));
        statusAssertionsWebClientPost(properties.getSendSms(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).sendAlert(any());
    }

}
