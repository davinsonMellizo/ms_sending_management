package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.*;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Push;
import co.com.bancolombia.model.message.Sms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PushAdapterTest {

    @InjectMocks
    private PushAdapter pushAdapter;

    @Mock
    private ConsumerProperties properties;

    @Mock
    private RestClient<Push, SuccessPush> client;

    @BeforeEach
    public void init(){
        String url = "localhost";
        when(properties.getResources()).thenReturn(new ConsumerProperties.Resources(url, url, url));
    }

    @Test
    public void sendSmsMasivianSuccessTest(){
        when(client.post(anyString(), any(), any(),any()))
                .thenReturn(Mono.just(SuccessPush.builder()
                        .data(new SuccessPush.Data(SuccessPush.SendMessageResponse
                                .builder()
                                .message("success")
                                .build()))
                        .build()));
        StepVerifier.create(pushAdapter.sendPush(new Push()))
                .assertNext(response -> response.getDescription().equals("success"))
                .verifyComplete();
    }

    @Test
    public void sendMAILErrorMasivianTest(){
        when(client.post(anyString(), any(), any(),any()))
                .thenReturn(Mono.error(Error.builder()
                        .httpsStatus(400)
                        .data(new ErrorPush("error", "error", new ArrayList<>()))
                        .build()));
        StepVerifier.create(pushAdapter.sendPush(new Push()))
                .assertNext(response -> response.getDescription().equals("error authentication"))
                .verifyComplete();
    }

    @Test
    public void sendMAILErrorWebClientTest(){
        when(client.post(anyString(), any(), any(),any()))
                .thenReturn(Mono.error(new Throwable("timeout")));
        StepVerifier.create(pushAdapter.sendPush(new Push()))
                .assertNext(response -> response.getDescription().equals("timeout"))
                .verifyComplete();
    }


}
