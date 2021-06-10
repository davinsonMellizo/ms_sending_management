package co.com.bancolombia.api.contact;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegrationTest;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.contact.ContactHandler;
import co.com.bancolombia.api.services.contact.ContactRouter;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.contact.Contact;
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

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CONTACT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;
import static org.mockito.ArgumentMatchers.any;
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
public class ContactRouterWithExceptionTest extends BaseIntegrationTest {

    @MockBean
    private ContactUseCase useCase;
    private String request;
    private final Contact contact = new Contact();

    @BeforeEach
    public void init() {
        contact.setIdContactMedium(1);
        contact.setIdEnrollmentContact(0);
        contact.setDocumentNumber(new Long(1061772353));
        contact.setDocumentType(0);
        contact.setValue("correo@gamail.com");
        contact.setIdState(0);

        request = loadFileConfig("contactRequest.json", String.class);
    }

    @Test
    public void saveContactsWithException() {
        when(useCase.saveContact(any())).thenReturn(Mono.error(new TechnicalException(INTERNAL_SERVER_ERROR)));
        statusAssertionsWebClientPost(properties.getSaveContacts(),
                request)
                .is5xxServerError();
        verify(useCase).saveContact(any());
    }

    @Test
    public void updateContactsWithException() {
        when(useCase.updateContact(any())).thenReturn(Mono.error(new BusinessException(CONTACT_NOT_FOUND)));
        statusAssertionsWebClientPut(properties.getUpdateContacts(),
                request)
                .is5xxServerError();
    }

    @Test
    public void deleteContactsWithException() {
        when(useCase.deleteContact(any())).thenReturn(Mono.error(new Exception()));
        final WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getDeleteContacts())
                .header("document-number", "1061772353")
                .header("document-type", "0")
                .header("contact-medium", "SMS")
                .header("enrollment-contact", "ALM")
                .exchange();
        spec.expectStatus().is5xxServerError();
    }

    @Test
    public void deleteContactsWithExceptionHeaders() {
        final WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getDeleteContacts())
                .exchange();
        spec.expectStatus().is5xxServerError();
    }
}
