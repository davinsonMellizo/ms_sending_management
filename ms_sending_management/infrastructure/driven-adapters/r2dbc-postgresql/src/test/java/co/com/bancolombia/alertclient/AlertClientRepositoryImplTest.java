package co.com.bancolombia.alertclient;

import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.response.StatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AlertClientRepositoryImplTest {

    @Autowired
    private AlertClientRepositoryImplement repositoryImplement;
    private final AlertClient alertClient = new AlertClient();

    @BeforeEach
    public void init() {
        alertClient.setIdAlert("HGD");
        alertClient.setDocumentNumber(10616L);
        alertClient.setIdDocumentType(0);
        alertClient.setNumberOperations(5);
        alertClient.setAmountEnable(9L);
        alertClient.setAccumulatedOperations(8L);
        alertClient.setAccumulatedAmount(1L);
        alertClient.setAssociationOrigin("ac");
        alertClient.setCreationUser("user");
    }

    @Test
    public void saveAlertTransaction() {
        StepVerifier.create(repositoryImplement.save(alertClient))
                .consumeNextWith(AlertSaved -> assertEquals(alertClient.getIdAlert(), AlertSaved.getIdAlert()))
                .verifyComplete();
    }

    @Test
    public void findAllAlertTransaction() {
        alertClient.setDocumentNumber(1061L);
        alertClient.setIdDocumentType(0);
        StepVerifier.create(repositoryImplement.findAllAlertsByClient(alertClient))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void updateAlertClient() {
        StepVerifier.create(repositoryImplement.updateAlertClient(StatusResponse.<AlertClient>builder()
                .before(alertClient).actual(alertClient)
                .build()))
                .consumeNextWith(response -> assertEquals(10616L, response.getActual().getDocumentNumber()))
                .verifyComplete();
    }

    @Test
    public void deleteAlertClient() {
        alertClient.setIdAlert("HGD");
        alertClient.setDocumentNumber(1061L);
        alertClient.setIdDocumentType(0);
        StepVerifier.create(repositoryImplement.delete(alertClient))
                .consumeNextWith(s -> assertEquals(alertClient.getIdAlert(), s))
                .verifyComplete();
    }

    @Test
    public void findAlertClient(){
        alertClient.setIdAlert("HGD");
        alertClient.setDocumentNumber(6565L);
        alertClient.setIdDocumentType(0);
        StepVerifier.create(repositoryImplement.findAlertClient(alertClient))
                .consumeNextWith(acFound -> assertEquals(alertClient.getDocumentNumber(), acFound.getDocumentNumber()))
                .verifyComplete();
    }
}
