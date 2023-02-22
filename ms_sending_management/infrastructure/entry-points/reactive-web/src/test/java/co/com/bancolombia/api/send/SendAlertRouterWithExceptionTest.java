package co.com.bancolombia.api.send;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.sendalert.SendAlertHandler;
import co.com.bancolombia.api.services.sendalert.SendAlertRouter;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.usecase.sendalert.SendingUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        SendAlertRouter.class,
        SendAlertHandler.class,
        ApiProperties.class,
        ExceptionHandler.class,
        ValidatorHandler.class
})
class SendAlertRouterWithExceptionTest extends BaseIntegration {

    @MockBean
    private SendingUseCase useCase;

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
    void saveAlertWithException() {
        when(useCase.alertSendingManager(any())).thenReturn(Mono.error(new TechnicalException(INTERNAL_SERVER_ERROR)));
        statusAssertionsWebClientPost(properties.getSend(), request)
                .isBadRequest();
        verify(useCase).alertSendingManager(any());
    }


}
