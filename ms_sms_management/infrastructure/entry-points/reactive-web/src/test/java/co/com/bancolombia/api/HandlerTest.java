package co.com.bancolombia.api;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.Template;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.SendAlertUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.TEMPLATE_NOT_FOUND;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_EXCEPTION;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class HandlerTest {

    @InjectMocks
    private Handler handler;

    @Mock
    private SendAlertUseCase useCase;

    @Mock
    private LogUseCase logUseCase;

    private Alert alert = new Alert();
    private Map<String, String> headers= Map.of("x-death", "count=1","retryNumber","1");
    BusinessException businessException = new BusinessException(TEMPLATE_NOT_FOUND);
    TechnicalException technicalException=new TechnicalException(TECHNICAL_EXCEPTION);
    @BeforeEach
    public void init() {
        alert.setProvider("MAS");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "bancolombia");
        alert.setTemplate(new Template( parameters, "Compra"));
        alert.setTrackId(UUID.randomUUID().toString());
        alert.setHeaders(headers);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void handleSendAlert() {
        when(useCase.sendAlert(any())).thenReturn(Mono.empty());
        StepVerifier.create(handler.sendAlert(alert))
                .verifyComplete();
    }

    @Test
    void handleExceptionsBusiness() {
        when(logUseCase.handlerLog(any(),anyString(),any(),anyBoolean()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(useCase.sendAlert(any())).thenReturn(Mono.error(businessException));
        StepVerifier.create(handler.sendAlert(alert))
                .expectError()
                .verify();
    }
    @Test
    void handleExceptionsTechnical() {
        when(logUseCase.handlerLog(any(),anyString(),any(),anyBoolean()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(useCase.sendAlert(any())).thenReturn(Mono.error(technicalException));
        StepVerifier.create(handler.sendAlert(alert))
                .expectError()
                .verify();
    }
    @Test
    void handleExceptionsThrowable() {
        when(logUseCase.handlerLog(any(),anyString(),any(),anyBoolean()))
                .thenReturn(Mono.just(Response.builder()
                        .code(200)
                        .description("success")
                        .build()));
        when(useCase.sendAlert(any())).thenReturn(Mono.error(new Throwable()));
        StepVerifier.create(handler.sendAlert(alert))
                .expectError()
                .verify();
    }
}
