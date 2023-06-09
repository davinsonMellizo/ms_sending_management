package co.com.bancolombia.log;


import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.log.data.LogMapper;
import co.com.bancolombia.log.reader.LogRepositoryReader;
import co.com.bancolombia.log.writer.LogRepository;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.QueryLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest("spring.application.name=ms_Log_management")
class LogRepositoryImplTest {

    @InjectMocks
    private LogRepositoryImplement logRepositoryImplement;
    @Mock
    private LogRepository repository;

    @Mock
    private LogRepositoryReader repositoryReader;
    @Spy
    private LogMapper mapper = Mappers.getMapper(LogMapper.class);
    private final Log log = new Log();

    @Test
    void saveLog() {
        logRepositoryImplement.saveLog(log)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void findLogTest() {
        when(repositoryReader.findAllLogByFilters(anyString(), anyString(), anyString(), anyString(), anyString(),
                any(), any())).thenReturn(Flux.just(new Log()));

        logRepositoryImplement.findLog(QueryLog.builder()
                        .consumer("").contactValue("").documentNumber("").documentType("")
                        .endDate(LocalDateTime.now()).endDate(LocalDateTime.now()).provider("")
                        .build())
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findLogErrorTest() {
        when(repositoryReader.findAllLogByFilters(anyString(), anyString(), anyString(), anyString(), anyString(),
                any(), any())).thenReturn(Flux.error(new Throwable()));

        logRepositoryImplement.findLog(QueryLog.builder()
                        .consumer("").contactValue("").documentNumber("").documentType("")
                        .endDate(LocalDateTime.now()).endDate(LocalDateTime.now()).provider("")
                        .build())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}