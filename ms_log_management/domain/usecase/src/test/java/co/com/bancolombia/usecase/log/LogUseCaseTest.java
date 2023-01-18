package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.QueryLog;
import co.com.bancolombia.model.log.gateways.LogGateway;
import co.com.bancolombia.model.log.gateways.RetrieveLogsGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogUseCaseTest {

    @InjectMocks
    private LogUseCase useCase;
    @Mock
    private LogGateway logGateway;
    @Mock
    private RetrieveLogsGateway retrieveLogsGateway;
    private final Log log = new Log();

    @Test
    public void saveLogTest() {
        when(logGateway.saveLog(any()))
                .thenReturn(Mono.just(log));
        StepVerifier
                .create(useCase.saveLog(log))
                .expectNextCount(0)
                .verifyComplete();
        verify(logGateway).saveLog(log);
    }

    @Test
    public void findLogHotTest() {
        when(logGateway.findLog(any()))
                .thenReturn(Mono.just(List.of(log)));
        QueryLog query = QueryLog.builder()
                .referenceDate(LocalDateTime.now().minusDays(90))
                .endDate(LocalDateTime.now())
                .build();
        StepVerifier
                .create(useCase.findLogsByDate(query))
                .expectNextCount(1)
                .verifyComplete();
        verify(logGateway).findLog(query);
    }


}
