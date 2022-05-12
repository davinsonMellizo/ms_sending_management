package co.com.bancolombia.client;


import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.response.StatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IClientRepositoryImplTest {

    @Autowired
    private ClientRepositoryImplement clientRepositoryImplement;

    private final Client client = new Client();

    @BeforeEach
    public void init() {
        client.setDocumentNumber(1061772353L);
        client.setDocumentType("0");
        client.setIdState(0);
        client.setCreationUser("username");
        client.setEnrollmentOrigin("ALM");
        client.setKeyMdm("key");
        client.setCreatedDate(LocalDateTime.now());
    }


    @Test
    public void findClientByDocument() {
        StepVerifier.create(clientRepositoryImplement.findClientByIdentification(client))
                .consumeNextWith(client -> assertEquals(1061772353, client.getDocumentNumber()))
                .verifyComplete();
    }

    @Test
    public void inactivateClient() {
        client.setId(0);
        StepVerifier.create(clientRepositoryImplement.inactivateClient(client))
                .consumeNextWith(client -> assertEquals(1061772353, client.getDocumentNumber()))
                .verifyComplete();
    }

    @Test
    public void saveClient() {
        client.setDocumentNumber(123456789L);
        clientRepositoryImplement.saveClient(client)
                .subscribe(contactSaved -> StepVerifier
                        .create(clientRepositoryImplement.findClientByIdentification(client))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    public void updateClient() {
        client.setId(0);
        StepVerifier.create(clientRepositoryImplement.updateClient(StatusResponse.<Client>builder()
                .before(client).actual(client)
                .build()))
                .consumeNextWith(response -> assertEquals(1061772353, response.getActual().getDocumentNumber()))
                .verifyComplete();
    }

}