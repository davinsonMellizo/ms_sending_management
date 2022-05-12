package co.com.bancolombia.api.priority;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.priority.PriorityHandler;
import co.com.bancolombia.api.services.priority.PriorityRouter;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.priority.Priority;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.priority.PriorityUseCase;
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

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.PRIORITY_NOT_FOUND;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        PriorityRouter.class,
        PriorityHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class PriorityRouterTest extends BaseIntegration {

    @MockBean
    private PriorityUseCase useCase;

    @MockBean
    private LoggerBuilder loggerBuilder;

    private String request;
    private final Priority priority = new Priority();
    private String url;
    private final static String ID = "/{id}";

    @BeforeEach
    public void init() {
        url = properties.getPriority();
        request = loadFileConfig("PriorityRequest.json", String.class);
    }

    @Test
    void save() {
        when(useCase.savePriority(any()))
                .thenReturn(Mono.just(priority));
        statusAssertionsWebClientPost(url, request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).savePriority(any());
    }

    @Test
    void findAll() {
        when(useCase.findAllByProvider(anyString()))
                .thenReturn(Mono.just(List.of(priority)));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(url + "-provider" + ID, "1")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findAllByProvider(anyString());
    }

    @Test
    void update() {
        when(useCase.updatePriority(any())).thenReturn(Mono.just(StatusResponse.<Priority>builder()
                .actual(priority).before(priority).build()));
        statusAssertionsWebClientPut(url,
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updatePriority(any());
    }

    @Test
    void delete() {
        when(useCase.deletePriorityById(any()))
                .thenReturn(Mono.just(1));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getPriority() + ID, "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deletePriorityById(any());
    }

    @Test
    void savePriorityWithException() {
        when(useCase.savePriority(any())).thenReturn(Mono.error(new TechnicalException(INTERNAL_SERVER_ERROR)));
        statusAssertionsWebClientPost(url,
                request)
                .is5xxServerError();
        verify(useCase).savePriority(any());
    }

    @Test
    void updatePriorityWithException() {
        when(useCase.updatePriority(any())).thenReturn(Mono.error(new BusinessException(PRIORITY_NOT_FOUND)));
        statusAssertionsWebClientPut(url,
                request)
                .is5xxServerError();
    }

}
