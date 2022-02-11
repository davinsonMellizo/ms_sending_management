package co.com.bancolombia.api.remitter;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.remitter.RemitterHandler;
import co.com.bancolombia.api.services.remitter.RemitterRouter;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.remitter.RemitterUseCase;
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
        RemitterRouter.class,
        RemitterHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class RemitterRouterTest extends BaseIntegration {

    @MockBean
    private RemitterUseCase useCase;

    @MockBean
    private LoggerBuilder loggerBuilder;

    private String request;
    private final Remitter remitter = new Remitter();
    private final static String ID = "/{id}";

    @BeforeEach
    public void init() {
        request = loadFileConfig("RemitterRequest.json", String.class);
    }

    @Test
    public void findRemitterById() {
        when(useCase.findRemitterById(any())).thenReturn(Mono.just(remitter));
        webTestClient.get().uri(properties.getRemitter() + ID, "0")
                .exchange()
                .expectStatus()
                .isOk();
        verify(useCase).findRemitterById(any());
    }

    @Test
    public void findAllRemitters() {
        when(useCase.findAllRemitter()).thenReturn(Mono.just(List.of(remitter)));
        webTestClient.get().uri(properties.getRemitter())
                .exchange()
                .expectStatus()
                .isOk();
        verify(useCase).findAllRemitter();
    }

    @Test
    public void saveRemitter() {
        when(useCase.saveRemitter(any())).thenReturn(Mono.just(remitter));
        statusAssertionsWebClientPost(properties.getRemitter(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveRemitter(any());
    }

    @Test
    public void updateRemitter() {
        when(useCase.updateRemitter(any())).thenReturn(Mono.just(StatusResponse.<Remitter>builder()
                .before(remitter)
                .actual(remitter)
                .build()));
        statusAssertionsWebClientPut(properties.getRemitter(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updateRemitter(any());
    }

    @Test
    public void deleteRemitter() {
        when(useCase.deleteRemitterById(any())).thenReturn(Mono.just(0));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getRemitter() + ID, "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteRemitterById(any());
    }

}
