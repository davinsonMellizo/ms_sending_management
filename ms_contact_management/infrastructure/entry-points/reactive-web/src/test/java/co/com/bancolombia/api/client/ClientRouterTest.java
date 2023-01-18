package co.com.bancolombia.api.client;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.dto.IdentificationDTO;
import co.com.bancolombia.api.mapper.EnrolMapper;
import co.com.bancolombia.api.services.client.ClientHandler;
import co.com.bancolombia.api.services.client.ClientRouter;
import co.com.bancolombia.log.LoggerBuilder;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.client.ResponseUpdateClient;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.usecase.client.ClientUseCase;
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
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ClientRouter.class,
        ClientHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class ClientRouterTest extends BaseIntegration {

    @MockBean
    private ClientUseCase useCase;
    @MockBean
    private EnrolMapper enrolMapper;
    @MockBean
    private LoggerBuilder loggerBuilder;

    private String request;
    private final Client client = new Client();
    private final Contact contact = new Contact();
    private final ResponseUpdateClient responseUpdateClient= new ResponseUpdateClient();

    @BeforeEach
    public void init() {
        client.setDocumentNumber(1061772353L);
        client.setDocumentType("0");
        client.setIdState(0);
        client.setCreationUser("username");
        client.setEnrollmentOrigin("ALM");
        client.setKeyMdm("key");
        client.setId(0);

        contact.setContactWay("");
        contact.setSegment("0");
        contact.setDocumentNumber(1061772353L);
        contact.setDocumentType("0");
        contact.setValue("correo@gamail.com");
        contact.setStateContact("0");

        request = loadFileConfig("clientRequest.json", String.class);
    }

    @Test
    void inactivateClient() {
        when(useCase.inactivateClient(any())).thenReturn(Mono.just(responseUpdateClient));
        final WebTestClient.ResponseSpec spec = webTestClient.put().uri(properties.getClient() + "/inactive")
                .bodyValue(IdentificationDTO.builder().documentNumber(1L).documentType("CC").build())
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).inactivateClient(any());
    }

    @Test
    void saveClient() {
        when(useCase.saveClientRequest(any(), anyBoolean(), anyString())).thenReturn(Mono.just(responseUpdateClient));
        when(enrolMapper.toEntity(any())).thenReturn(Enrol.builder().build());
        statusAssertionsWebClientPost(properties.getClient(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveClientRequest(any(), anyBoolean(), anyString());
    }

    @Test
    void deleteClient() {
        when(useCase.deleteClient(anyLong(), anyLong())).thenReturn(Mono.just(1));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getClient() + "/delete-range")
                .header("document-number-init", "1061772353")
                .header("document-number-end", "1231333")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteClient(anyLong(), anyLong());
    }

}
