package co.com.bancolombia.api.alerttemplate;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.alerttemplate.AlertTemplateHandler;
import co.com.bancolombia.api.services.alerttemplate.AlertTemplateRouter;
import co.com.bancolombia.config.model.alerttemplate.AlertTemplate;
import co.com.bancolombia.usecase.alerttemplate.AlertTemplateUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        AlertTemplateRouter.class,
        AlertTemplateHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class AlertTemplateRouterTest extends BaseIntegration {

    @MockBean
    private AlertTemplateUseCase useCase;
    private String request;
    private String url;
    private final AlertTemplate alertTemplate = new AlertTemplate();

    private final static String ID = "/{id}";

    @BeforeEach
    public void init() {
        url = properties.getAlert() + "-template";
        request = loadFileConfig("AlertTemplateRequest.json", String.class);
    }

    @Test
    public void findAlertTemplateById() {
        when(useCase.findAlertTemplateById(any())).thenReturn(Mono.just(alertTemplate));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(url + ID, "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findAlertTemplateById(any());
    }

    @Test
    public void save() {
        when(useCase.saveAlertTemplate(any())).thenReturn(Mono.just(alertTemplate));
        statusAssertionsWebClientPost(url,
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveAlertTemplate(any());
    }

    @Test
    public void delete() {
        when(useCase.deleteAlertTemplateById(any())).thenReturn(Mono.just(1));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(url + ID, "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteAlertTemplateById(any());
    }

    @Test
    public void deleteAlertTemplateWithException() {
        when(useCase.deleteAlertTemplateById(any())).thenReturn(Mono.error(new Exception()));
        final WebTestClient.ResponseSpec spec = webTestClient.delete().uri(url + ID, "xx")
                .exchange();
        spec.expectStatus().is5xxServerError();
    }

}
