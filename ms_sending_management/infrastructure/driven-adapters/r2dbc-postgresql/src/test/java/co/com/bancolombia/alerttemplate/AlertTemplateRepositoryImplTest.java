package co.com.bancolombia.alerttemplate;

import co.com.bancolombia.model.alerttemplate.AlertTemplate;
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
public class AlertTemplateRepositoryImplTest {

    @Autowired
    private AlertTemplateRepositoryImplement repositoryImpl;
    private final AlertTemplate alertTemplate = new AlertTemplate();

    @BeforeEach
    public void init() {
        alertTemplate.setId(0);
        alertTemplate.setField("field1");
        alertTemplate.setInitialPosition(1);
        alertTemplate.setFinalPosition(5);
        alertTemplate.setCreationUser("User");
    }

    @Test
    public void findTemplateId() {
        StepVerifier.create(repositoryImpl.findTemplateById(alertTemplate.getId()))
                .consumeNextWith(atFound -> assertEquals(alertTemplate.getId(), atFound.getId()))
                .verifyComplete();
    }

    @Test
    public void saveAlertTemplate() {
        alertTemplate.setId(3);
        repositoryImpl.save(alertTemplate)
                .subscribe(alertSaved -> StepVerifier
                        .create(repositoryImpl.findTemplateById(alertSaved.getId()))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    public void deleteAlertTemplate() {
        alertTemplate.setId(1);
        StepVerifier.create(repositoryImpl.delete(alertTemplate.getId()))
                .consumeNextWith(s -> assertEquals(alertTemplate.getId(), s))
                .verifyComplete();
    }
}
