package co.com.bancolombia.api.contact;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegrationTest;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.alert.AlertHandler;
import co.com.bancolombia.api.services.alert.AlertRouter;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.response.ContactsResponse;
import co.com.bancolombia.usecase.alert.AlertUseCase;
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
        AlertRouter.class,
        AlertHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class AlertRouterTest extends BaseIntegrationTest {

    @MockBean
    private AlertUseCase useCase;
    private String request;
    private final Alert alert = new Alert();

    @BeforeEach
    public void init() {
        alert.setIdContactMedium(1);
        alert.setIdEnrollmentContact(0);
        alert.setDocumentNumber(new Long(1061772353));
        alert.setDocumentType(0);
        alert.setValue("correo@gamail.com");
        alert.setIdState(0);

        request = loadFileConfig("contactRequest.json", String.class);
    }

    @Test
    public void findAllContactsByClient() {
        when(useCase.findAlertById(any())).thenReturn(Mono.just(ContactsResponse.builder()
                .alerts(List.of(alert)).build()));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(properties.getFindAlert())
                .header("document-number", "1061772353")
                .header("document-type", "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findAlertById(any());
    }

    @Test
    public void saveContacts() {
        when(useCase.saveAlert(any())).thenReturn(Mono.just(alert));
        statusAssertionsWebClientPost(properties.getSaveAlert(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult()
                .getResponseBody();
        verify(useCase).saveAlert(any());
    }

    @Test
    public void updateContacts() {
        when(useCase.updateAlert(any())).thenReturn(Mono.just(ContactsResponse.builder()
                .alerts(List.of(alert)).build()));
        statusAssertionsWebClientPut(properties.getUpdateAlert(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult()
                .getResponseBody();
        verify(useCase).updateAlert(any());
    }

    @Test
    public void deleteContacts() {
        when(useCase.deleteAlert(any())).thenReturn(Mono.just(1));
        final WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getDeleteAlert())
                .header("document-number", "1061772353")
                .header("document-type", "0")
                .header("contact-medium", "SMS")
                .header("enrollment-contact", "ALM")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteAlert(any());
    }

}
