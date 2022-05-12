package co.com.bancolombia.alert;

import co.com.bancolombia.model.alert.Alert;
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
class AlertRepositoryImplTest {

    @Autowired
    private AlertRepositoryImplement AlertRepositoryImplement;
    private final Alert alert = new Alert();

    @BeforeEach
    public void init() {
        alert.setId("HGD");
        alert.setIdProviderMail("FGH");
        alert.setIdProviderSms("FGH");
        alert.setIdRemitter(0);
        alert.setTemplateName("Compra");
        alert.setIdState(0);
        alert.setIdCategory(1);
        alert.setAttentionLine("3215684");
        alert.setCreatedDate(LocalDateTime.now());
        alert.setCreationUser("UserName");
        alert.setVisibleChannel(true);
        alert.setObligatory(true);
        alert.setSubjectMail("sub");
        alert.setBasicKit(true);
        alert.setPriority(1);
        alert.setNature("NM");
        alert.setPush("SI");
        alert.setMessage("message");
        alert.setDescription("alert");

    }

    @Test
    void findIdAlert() {
        StepVerifier.create(AlertRepositoryImplement.findAlertById(alert.getId()))
                .consumeNextWith(alertFound -> assertEquals(alert.getId(), alertFound.getId()))
                .verifyComplete();
    }
}