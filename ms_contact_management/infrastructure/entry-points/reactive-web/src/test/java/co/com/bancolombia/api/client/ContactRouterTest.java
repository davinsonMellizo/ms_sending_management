package co.com.bancolombia.api.client;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.mapper.EnrolMapper;
import co.com.bancolombia.api.services.contact.ContactHandler;
import co.com.bancolombia.api.services.contact.ContactRouter;
import co.com.bancolombia.log.LoggerBuilder;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.ResponseContacts;
import co.com.bancolombia.usecase.contact.ContactUseCase;
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
class ContactRouterTest extends BaseIntegration {

    @MockBean
    private ContactUseCase contactUseCase;
    @MockBean
    private EnrolMapper enrolMapper;
    @MockBean
    private LoggerBuilder loggerBuilder;

    private String request;
    private final Contact contact = new Contact();

    @BeforeEach
    public void init() {
        contact.setContactWay("");
        contact.setSegment("0");
        contact.setDocumentNumber(1061772353L);
        contact.setDocumentType("0");
        contact.setValue("correo@gamail.com");
        contact.setStateContact("0");

        request = loadFileConfig("clientRequest.json", String.class);
    }

    @Test
    void findAllContactsByClient() {
        when(contactUseCase.findContactsByClient(any(), anyString())).thenReturn(Mono.just(ResponseContacts.<Contact>builder()
                .contacts(List.of(contact))
                .documentNumber(contact.getDocumentNumber())
                .documentType(contact.getDocumentType())
                .build()));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(properties.getClient())
                .header("document-number", "1061772353")
                .header("document-type", "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(contactUseCase).findContactsByClient(any(), anyString());
    }
}
