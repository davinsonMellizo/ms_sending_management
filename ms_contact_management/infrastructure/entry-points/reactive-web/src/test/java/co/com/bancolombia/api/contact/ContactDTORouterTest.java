package co.com.bancolombia.api.contact;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegrationTest;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.contact.ContactHandler;
import co.com.bancolombia.api.services.contact.ContactRouter;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.ResponseContacts;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.contact.ContactUseCase;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ContactRouter.class,
        ContactHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class ContactDTORouterTest extends BaseIntegrationTest {

    @MockBean
    private ContactUseCase useCase;
    private String request;
    private String requestSave;
    private final Contact contact = new Contact();

    @BeforeEach
    public void init() {
        contact.setContactMedium("");
        contact.setSegment("0");
        contact.setDocumentNumber(new Long(1061772353));
        contact.setDocumentType("0");
        contact.setValue("correo@gamail.com");
        contact.setState("0");

        request = loadFileConfig("contactRequest.json", String.class);
        requestSave = loadFileConfig("contactSaveRequest.json", String.class);
    }

    @Test
    public void findAllContactsByClient() {
        when(useCase.findContactsByClient(any(), anyString())).thenReturn(Mono.just(ResponseContacts.<Contact>builder()
                .contacts(List.of(contact))
                .documentNumber(contact.getDocumentNumber())
                .documentType(contact.getDocumentType())
                .build()));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(properties.getContact())
                .header("document-number", "1061772353")
                .header("document-type", "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findContactsByClient(any(), anyString());
    }

    @Test
    public void saveContact() {
        when(useCase.saveContact(any())).thenReturn(Mono.just(contact));
        statusAssertionsWebClientPost(properties.getContact(),
                requestSave)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult()
                .getResponseBody();
        verify(useCase).saveContact(any());
    }

    @Test
    public void updateContact() {
        when(useCase.updateContactRequest(any())).thenReturn(Mono.just(StatusResponse.<Contact>builder()
                .actual(contact).before(contact).build()));
        statusAssertionsWebClientPut(properties.getContact(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult()
                .getResponseBody();
        verify(useCase).updateContactRequest(any());
    }

    @Test
    public void deleteContacts() {
        when(useCase.deleteContact(any())).thenReturn(Mono.just(1));
        final WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getContact())
                .header("document-number", "1061772353")
                .header("document-type", "0")
                .header("contact-medium", "SMS")
                .header("consumer", "ALM")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteContact(any());
    }

}
