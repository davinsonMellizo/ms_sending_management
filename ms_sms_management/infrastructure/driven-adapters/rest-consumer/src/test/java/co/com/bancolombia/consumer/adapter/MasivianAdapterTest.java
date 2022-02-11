package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.*;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Sms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MasivianAdapterTest {

    @InjectMocks
    private MasivianAdapter masivianAdapter;

    @Mock
    private ConsumerProperties properties;
    @Mock
    private RestClient<Sms, SuccessMasivianSMS> client;

    @BeforeEach
    public void init(){
        String url = "localhost";
        when(properties.getResources()).thenReturn(new ConsumerProperties.Resources(url, url));
    }

    @Test
    public void sendSmsMasivianSuccessTest(){
        when(client.post(anyString(), any(), any(),any()))
                .thenReturn(Mono.just(SuccessMasivianSMS.builder()
                        .deliveryToken("success")
                        .build()));
        StepVerifier.create(masivianAdapter.sendSMS(new Sms()))
                .assertNext(response -> response.getDescription().equals("success"))
                .verifyComplete();
    }

    @Test
    public void sendSmsErrorMasivianTest(){
        when(client.post(anyString(), any(), any(),any()))
                .thenReturn(Mono.error(Error.builder()
                        .httpsStatus(400)
                        .data(new ErrorMasivianSMS("error", "error authentication"))
                        .build()));
        StepVerifier.create(masivianAdapter.sendSMS(new Sms()))
                .assertNext(response -> response.getDescription().equals("error authentication"))
                .verifyComplete();
    }

    @Test
    public void sendSmsErrorWebClientTest(){
        when(client.post(anyString(), any(), any(),any()))
                .thenReturn(Mono.error(new Throwable("timeout")));
        StepVerifier.create(masivianAdapter.sendSMS(new Sms()))
                .assertNext(response -> response.getDescription().equals("timeout"))
                .verifyComplete();
    }


}
