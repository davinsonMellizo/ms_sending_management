package co.com.bancolombia.log;


import co.com.bancolombia.log.data.LogMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.log.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;


@ExtendWith(SpringExtension.class)
@SpringBootTest("spring.application.name=ms_Log_management")
public class LogRepositoryImplWithExceptionTest {

    @InjectMocks
    private LogRepositoryImplement logRepositoryImplement;
    @Mock
    private LogRepository repository;
    @Spy
    private LogMapper mapper = Mappers.getMapper(LogMapper.class);
    private final Log log= new Log();

    @Test
    public void findStateByNameWithException() {

        logRepositoryImplement.saveLog(log)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

}