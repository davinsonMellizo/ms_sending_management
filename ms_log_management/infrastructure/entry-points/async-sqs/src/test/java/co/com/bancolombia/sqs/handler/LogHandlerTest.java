package co.com.bancolombia.sqs.handler;

import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.usecase.log.LogUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import static org.assertj.core.api.Assertions.assertThatCode;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogHandlerTest {



    @InjectMocks
    private LogHandlerLocal sqsServiceLocal;
    @InjectMocks
    private LogHandler sqsService;

    @Mock
    private LogUseCase useCase;

    @Mock
    private ObjectMapper objectMapper;
    private final Log log = new Log();

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        Mockito.when(useCase.saveLog(any())).thenReturn(Mono.empty());
    }

    @Test
    public void createLocalTest() throws JsonProcessingException {
        assertThatCode(() -> sqsServiceLocal.listenLogBySqsListener(objectMapper.writeValueAsString(log)));
    }

    @Test
    public void createTest() throws JsonProcessingException {
        assertThatCode(() -> sqsService.listenLogByQueueListener((log)));
    }

}