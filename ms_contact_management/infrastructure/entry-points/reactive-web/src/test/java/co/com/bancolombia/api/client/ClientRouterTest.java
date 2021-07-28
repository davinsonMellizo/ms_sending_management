package co.com.bancolombia.api.client;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegrationTest;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.client.ClientHandler;
import co.com.bancolombia.api.services.client.ClientRouter;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.response.StatusResponse;
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
public class ClientRouterTest extends BaseIntegrationTest {

    @MockBean
    private ClientUseCase useCase;
    private String request;
    private final Client client = new Client();

    @BeforeEach
    public void init() {
        client.setDocumentNumber(1061772353L);
        client.setDocumentType("0");
        client.setIdState(0);
        client.setCreationUser("username");
        client.setEnrollmentOrigin("ALM");
        client.setKeyMdm("key");

        request = loadFileConfig("clientRequest.json", String.class);
    }

    @Test
    public void findAllContactsByClient() {
        when(useCase.findClientByIdentification(any())).thenReturn(Mono.just(client));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(properties.getClient())
                .header("document-number", "1061772353")
                .header("document-type", "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findClientByIdentification(any());
    }

    @Test
    public void saveContacts() {
        when(useCase.saveClient(any())).thenReturn(Mono.just(client));
        statusAssertionsWebClientPost(properties.getClient(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveClient(any());
    }

    @Test
    public void updateContacts() {
        when(useCase.updateClient(any())).thenReturn(Mono.just(StatusResponse.<Client>builder()
                .actual(client).before(client).build()));
        statusAssertionsWebClientPut(properties.getClient(),
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updateClient(any());
    }

    @Test
    public void deleteContacts() {
        when(useCase.deleteClient(any())).thenReturn(Mono.just(client));
        final WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getClient())
                .header("document-number", "1061772353")
                .header("document-type", "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteClient(any());
    }

}
