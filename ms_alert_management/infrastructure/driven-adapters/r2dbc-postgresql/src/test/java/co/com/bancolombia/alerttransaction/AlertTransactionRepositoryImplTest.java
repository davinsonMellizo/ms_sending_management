package co.com.bancolombia.alerttransaction;

import co.com.bancolombia.model.alerttransaction.AlertTransaction;
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
public class AlertTransactionRepositoryImplTest {

    @Autowired
    private AlertTransactionRepositoryImplement repositoryImplement;
    private final AlertTransaction alert = new AlertTransaction();

    @BeforeEach
    public void init() {
        alert.setIdAlert("HGD");
        alert.setIdTransaction("0358");
        alert.setIdConsumer("ALM");
    }

    @Test
    void findAllAlertTransaction() {
        StepVerifier.create(repositoryImplement.findAllAlertTransaction(alert.getIdAlert()))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void saveAlertTransaction() {
        StepVerifier.create(repositoryImplement.saveAlertTransaction(alert))
                .consumeNextWith(AlertSaved ->assertEquals(alert.getIdAlert(), AlertSaved.getIdAlert()))
                .verifyComplete();
    }

    @Test
    void deleteAlertTransaction() {
        alert.setIdTransaction("0256");
        StepVerifier.create(repositoryImplement.deleteAlertTransaction(alert))
                .consumeNextWith(s -> assertEquals(alert.getIdAlert(), s))
                .verifyComplete();
    }
}