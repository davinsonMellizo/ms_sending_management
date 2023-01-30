package co.com.bancolombia.api.clientmacd;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.mapper.EnrolMapper;
import co.com.bancolombia.api.services.clientmacd.ClientMcdHandler;
import co.com.bancolombia.api.services.clientmacd.ClientMcdRouter;
import co.com.bancolombia.log.LoggerBuilder;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.client.ClientUseCase;
import co.com.bancolombia.usecase.contact.ContactUseCase;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ClientMcdRouter.class,
        ClientMcdHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
class ClientMcdRouterTest extends BaseIntegration {

    @MockBean
    private ClientUseCase useCase;
    @MockBean
    private ContactUseCase contactUseCase;
    @MockBean
    private EnrolMapper enrolMapper;
    @MockBean
    private LoggerBuilder loggerBuilder;

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
        client.setId(0);

        request = loadFileConfig("clientRequest.json", String.class);
    }

    @Test
    void updateClient() {
        when(enrolMapper.toEntity(any())).thenReturn(Enrol.builder().build());
        when(useCase.updateClientMcd(any(), anyBoolean())).thenReturn(Mono.just(StatusResponse.<Enrol>builder()
                .actual(Enrol.builder().client(client).build()).before(Enrol.builder().client(client).build()).build()));
        statusAssertionsWebClientPut(properties.getClient() + "-macd",
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updateClientMcd(any(), anyBoolean());
    }

}
