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

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogHandler {



    @InjectMocks
    private LogHandlerLocal sqsService;

    @Mock
    private LogUseCase useCase;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        Mockito.when(useCase.saveLog(any())).thenReturn(Mono.empty());
        //Mockito.when(createMessageUseCase.createFailMessage(DataTest.FAIL_MESSAGE)).thenReturn(Mono.just(DataTest.FAIL_MESSAGE));
    }

    @Test
    public void createTest() throws JsonProcessingException {
        assertThatCode(() -> sqsService.listenLogBySqsListener(objectMapper.writeValueAsString(new Log())))
                .isNotNull();
    }

}