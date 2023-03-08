package co.com.bancolombia.api.log;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.services.log.HandlerLog;
import co.com.bancolombia.api.services.log.RouterLog;
import co.com.bancolombia.commons.enums.BusinessErrorMessage;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.log.LoggerBuilder;
import co.com.bancolombia.usecase.log.LogUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
        RouterLog.class,
        HandlerLog.class,
        ApiProperties.class,
        ExceptionHandler.class
})
public class LogRouterTest{

    @MockBean
    private LogUseCase useCase;
    @MockBean
    private LoggerBuilder loggerBuilder;

    @Autowired
    protected ApiProperties properties;

    @Autowired
    protected WebTestClient webTestClient;

    @Test
    void findAllContactsByClient() {
        when(useCase.findLogsByDate(any())).thenReturn(Mono.just(List.of()));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(properties.getLog())
                .header("document-number", "1061772353")
                .header("document-type", "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findLogsByDate(any());
    }

    @Test
    void findAllContactsByClientTechExc() {
        when(useCase.findLogsByDate(any())).thenReturn(Mono.error(new TechnicalException(TechnicalExceptionEnum.FIND_LOG_ERROR)));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(properties.getLog())
                .header("document-number", "1061772353")
                .header("document-type", "0")
                .exchange();
        spec.expectStatus().is5xxServerError();
    }

    @Test
    void findAllContactsByClientBisExc() {
        when(useCase.findLogsByDate(any())).thenReturn(Mono.error(new BusinessException(BusinessErrorMessage.INVALID_DATA)));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(properties.getLog())
                .header("document-number", "1061772353")
                .header("document-type", "0")
                .exchange();
        spec.expectStatus().is5xxServerError();
    }

    @Test
    void findAllContactsByClientThrExc() {
        when(useCase.findLogsByDate(any())).thenReturn(Mono.error(new Throwable("Error")));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(properties.getLog())
                .header("document-number", "1061772353")
                .header("document-type", "0")
                .exchange();
        spec.expectStatus().is5xxServerError();
    }

}
