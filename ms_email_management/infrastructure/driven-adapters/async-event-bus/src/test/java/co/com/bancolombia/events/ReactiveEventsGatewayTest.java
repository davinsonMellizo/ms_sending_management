package co.com.bancolombia.events;

import co.com.bancolombia.model.log.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.async.api.DirectAsyncGateway;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReactiveEventsGatewayTest {

    @InjectMocks
    private ReactiveDirectAsyncGateway reactiveDirectAsyncGateway;

    @Mock
    private DirectAsyncGateway directAsyncGateway;

   @BeforeEach
    public void init(){
       when(directAsyncGateway.sendCommand( any(), anyString())).thenReturn(Mono.empty());
   }

   @Test
    void senEventLogTest(){
       StepVerifier.create(reactiveDirectAsyncGateway.sendCommanLogEmail(new Log())).verifyComplete();
   }

}
