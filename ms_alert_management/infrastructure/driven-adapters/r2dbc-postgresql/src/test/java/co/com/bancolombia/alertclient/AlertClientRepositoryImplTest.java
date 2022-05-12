package co.com.bancolombia.alertclient;

import co.com.bancolombia.model.alertclient.AlertClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AlertClientRepositoryImplTest {

    @Autowired
    private AlertClientRepositoryImplement repositoryImplement;
    private final AlertClient alertClient = new AlertClient();


    @BeforeEach
    public void init() {
        alertClient.setIdAlert("HGS");
        alertClient.setDocumentNumber(1061772353L);
        alertClient.setDocumentType(0);
        alertClient.setNumberOperations(5);
        alertClient.setAmountEnable(9L);
        alertClient.setAccumulatedOperations(8);
        alertClient.setAccumulatedAmount(1L);
        alertClient.setAssociationOrigin("ac");
        alertClient.setCreationUser("user");
    }
    @Test
    void saveAlertClient() {
        StepVerifier.create(repositoryImplement.save(List.of(alertClient)))
                .consumeNextWith(AlertSaved -> assertEquals(alertClient.getIdAlert(), AlertSaved.getIdAlert()))
                .verifyComplete();
    }

    @Test
    void findAllAlertClient() {
        StepVerifier.create(repositoryImplement.alertsVisibleChannelByClient(1061772353L, 0))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void updateAlertClient() {
        StepVerifier.create(repositoryImplement.updateAlertClient(alertClient))
                .consumeNextWith(response -> assertEquals(1061772353L, response.getDocumentNumber()))
                .verifyComplete();
    }

    @Test
    void deleteAlertClient() {
        alertClient.setIdAlert("HGD");
        alertClient.setDocumentNumber(1061772353L);
        alertClient.setDocumentType(0);
        StepVerifier.create(repositoryImplement.delete(alertClient))
                .consumeNextWith(alertClient1 -> assertEquals(alertClient.getIdAlert(), alertClient1.getIdAlert()))
                .verifyComplete();
    }

    @Test
    void findAlertClient(){
        alertClient.setIdAlert("UPD");
        alertClient.setDocumentNumber(1061772353L);
        alertClient.setDocumentType(0);
        StepVerifier.create(repositoryImplement.findAlertClient(alertClient))
                .consumeNextWith(acFound -> assertEquals(alertClient.getIdAlert(), acFound.getIdAlert()))
                .verifyComplete();
    }
}
