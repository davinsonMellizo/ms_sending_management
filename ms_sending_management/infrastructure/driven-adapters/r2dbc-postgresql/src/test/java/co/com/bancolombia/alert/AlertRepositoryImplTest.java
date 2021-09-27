package co.com.bancolombia.alert;

import co.com.bancolombia.config.model.alert.Alert;
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
public class AlertRepositoryImplTest {

    @Autowired
    private AlertRepositoryImplement AlertRepositoryImplement;
    private final Alert alert = new Alert();

    @BeforeEach
    public void init() {
        alert.setId("HGD");
        alert.setIdProviderMail("HJK");
        alert.setIdProviderSms("JKL");
        alert.setIdRemitter(0);
        alert.setIdService(0);
        alert.setIdTemplate(0);
        alert.setIdState(0);
        alert.setAttentionLine("3215684");
        alert.setCreatedDate(LocalDateTime.now());
        alert.setCreationUser("UserName");
        alert.setVisibleChannel(true);
        alert.setObligatory(true);
        alert.setSubjectMail("sub");
        alert.setPriority(0);
        alert.setPathAttachedMail("path");
        alert.setNature("NM");
        alert.setMessage("message");
        alert.setDescription("alert");

    }

    @Test
    public void findIdAlert() {
        StepVerifier.create(AlertRepositoryImplement.findAlertById(alert.getId()))
                .consumeNextWith(alertFound -> assertEquals(alert.getId(), alertFound.getId()))
                .verifyComplete();
    }

    @Test
    public void updateAlert() {
        alert.setIdState(1);
        alert.setId("UPD");
        StepVerifier.create(AlertRepositoryImplement.updateAlert(alert))
                .consumeNextWith(status -> assertEquals(alert.getId(), status.getActual().getId()))
                .verifyComplete();
    }

    @Test
    public void saveAlert() {
        alert.setId("ASL");
        AlertRepositoryImplement.saveAlert(alert)
                .subscribe(AlertSaved -> StepVerifier
                        .create(AlertRepositoryImplement.findAlertById(AlertSaved.getId()))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    public void deleteAlert() {
        StepVerifier.create(AlertRepositoryImplement.deleteAlert(alert.getId()))
                .consumeNextWith(s -> assertEquals(alert.getId(), s))
                .verifyComplete();
    }
}