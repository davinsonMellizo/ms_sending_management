package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.ErrorInalambriaSMS;
import co.com.bancolombia.consumer.adapter.response.SuccessInalambriaSMS;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.SMSInalambria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InalambriaAdapterTest {

    @InjectMocks
    private InalambriaAdapter inalambriaAdapter;

    @Mock
    private ConsumerProperties properties;
    @Mock
    private RestClient<SMSInalambria, SuccessInalambriaSMS> client;
    @Mock
    private SMSInalambria sms;

    @BeforeEach
    public void init() {
        String url = "localhost";
        //when(properties.getResources()).thenReturn(new ConsumerProperties.Resources(url, url, url, url, url, url));
        sms = new SMSInalambria();
        sms.setHeaders(Map.of("header", "test"));
    }

    @Test
    void sendSmsInalambriaSuccessTest() {
        when(client.post(anyString(), any(), any(), any()))
                .thenReturn(Mono.just(SuccessInalambriaSMS.builder()
                        .messageText("success")
                        .build()));
        StepVerifier
                .create(inalambriaAdapter.sendSMS(new SMSInalambria()))
                .assertNext(response -> response.getDescription().equals("success"))
                .verifyComplete();
    }

    @Test
    void sendSmsErrorInalambriaTest() {
        when(client.post(anyString(), any(), any(), any()))
                .thenReturn(Mono.error(Error.builder()
                        .httpsStatus(400)
                        .data(new ErrorInalambriaSMS("error authentication", 400, 123L))
                        .build()));

        StepVerifier.create(inalambriaAdapter.sendSMS(sms))
                .assertNext(response -> response.getDescription().equals("error authentication"))
                .verifyComplete();
    }

    @Test
    void sendSmsErrorWebClientTest() {
        when(client.post(anyString(), any(), any(), any()))
                .thenReturn(Mono.error(new Throwable("123timeout")));
        StepVerifier
                .create(inalambriaAdapter.sendSMS(new SMSInalambria()))
                .assertNext(response -> response.getDescription().equals(123))
                .verifyComplete();
    }


}
