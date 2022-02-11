/*package co.com.bancolombia.log;


import co.com.bancolombia.model.log.Log;
import com.sun.source.tree.ModuleTree;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest("spring.application.name=ms_Log_management")
public class LogRepositoryImplTest {

    @Autowired
    private LogRepositoryImplement logRepositoryImplement;

    private final Log log = new Log();

    @Test
    public void saveLog() {
        log.setDocumentType(0);
        log.setDocumentNumber(111L);
        log.setAlertId("A");
        log.setIdAlert("A");
        log.setAlertType("T");
        log.setAlertDestination("D");
        log.setMessageType("T");
        log.setMessageSent("S");
        log.setAccountType("T");
        log.setAccountNumber(1);
        log.setOperationChannel("C");
        log.setOperationDescription("D");
        log.setOperationCode("C");
        log.setOperationNumber(1);
        log.setEnabledAmount(1L);
        log.setSendResponseCode(1);
        log.setSendResponseDescription("D");
        log.setPriority(1);
        log.setTemplate("D");
        log.setAlertIndicator(1);
        log.setIndicatorDescription("D");
        log.setApplicationCode("A");
        StepVerifier.create(logRepositoryImplement.saveLog(log))
                        .consumeNextWith(log1 -> assertEquals(log.getDocumentNumber(),log1.getDocumentNumber()))
                        .verifyComplete();
    }
}*/