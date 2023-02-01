package co.com.bancolombia.usecase.log;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationLogUtilTest {

    @Mock
    private LogUseCase logUseCase;
    private Alert alert = new Alert();
    private Response response = new Response();
    private Map<String, String> headers= Map.of("x-death", "count=1","retryNumber","1");
    @BeforeEach
    public void init (){
        response.setCode(1);
        response.setDescription("description");
        alert.setLogKey(UUID.randomUUID().toString());
        alert.setProvider("INA");
        alert.setTo("number");
        alert.setHeaders(headers);


    }

    @Test
    void validSendLog(){
        when(logUseCase.sendLog(any(),anyString(),any()))
                .thenReturn(Mono.empty());
        StepVerifier
                .create(ValidationLogUtil.validSendLog(alert,"SMS",response,logUseCase))
                .expectError()
                .verify();


    }

<<<<<<< HEAD
}
=======
}
>>>>>>> trunk
