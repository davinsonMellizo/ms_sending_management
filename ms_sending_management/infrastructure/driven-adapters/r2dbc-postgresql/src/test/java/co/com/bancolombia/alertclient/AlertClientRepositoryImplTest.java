package co.com.bancolombia.alertclient;

import co.com.bancolombia.model.alertclient.AlertClient;
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
class AlertClientRepositoryImplTest {

    @Autowired
    private AlertClientRepositoryImplement repositoryImplement;
    private final AlertClient alertClient = new AlertClient();

    @BeforeEach
    public void init() {
        alertClient.setIdAlert("HGD");
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
    void accumulateAlertClient() {
        StepVerifier.create(repositoryImplement.accumulate(alertClient))
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test
    void findAlertClient(){
        alertClient.setIdAlert("HGD");
        alertClient.setDocumentNumber(1061772353L);
        alertClient.setDocumentType(0);
        StepVerifier.create(repositoryImplement.findAlertClient(alertClient))
                .consumeNextWith(acFound -> assertEquals(alertClient.getIdAlert(), acFound.getIdAlert()))
                .verifyComplete();
    }
}
