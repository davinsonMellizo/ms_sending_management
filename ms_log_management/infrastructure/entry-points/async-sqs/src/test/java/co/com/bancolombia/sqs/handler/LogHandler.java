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
public class LogHandler {



    @InjectMocks
    private LogHandlerLocal sqsService;

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
    public void createTest() throws JsonProcessingException {
        assertThatCode(() -> sqsService.listenLogBySqsListener(objectMapper.writeValueAsString(log)))
                .isNotNull();
    }

}