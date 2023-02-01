package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.ErrorMasivianSMS;
import co.com.bancolombia.consumer.adapter.response.SuccessMasivianSMS;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.SMSMasiv;
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
class MasivianAdapterTest {

    @InjectMocks
    private MasivAdapter masivianAdapter;

    @Mock
    private ConsumerProperties properties;
    @Mock
    private RestClient<SMSMasiv, SuccessMasivianSMS> client;

    @BeforeEach
    public void init() {
        String url = "localhost";
        when(properties.getResources()).thenReturn(new ConsumerProperties.Resources(url, url, url, url, url, url, url, url));
    }

    @Test
    void sendSmsMasivianSuccessTest() {
        when(client.post(anyString(), any(), any(), any()))
                .thenReturn(Mono.just(SuccessMasivianSMS.builder().statusMessage("Message acepted for delivery")
                        .build()));
        StepVerifier.create(masivianAdapter.sendSMS(new SMSMasiv()))
                .assertNext(response -> response.getDescription().equals("success"))
                .verifyComplete();
    }

    @Test
    void sendSmsErrorMasivianTest() {
        when(client.post(anyString(), any(), any(), any()))
                .thenReturn(Mono.error(Error.builder()
                        .httpsStatus(400)
                        .data(new ErrorMasivianSMS("error", "error authentication"))
                        .build()));
        StepVerifier.create(masivianAdapter.sendSMS(new SMSMasiv()))
                .assertNext(response -> response.getDescription().equals("error authentication"))
                .verifyComplete();
    }

    @Test
    void sendSmsErrorWebClientTest() {
        when(client.post(anyString(), any(), any(), any()))
                .thenReturn(Mono.error(new Throwable("123timeout")));
        StepVerifier.create(masivianAdapter.sendSMS(new SMSMasiv()))
                .assertNext(response -> response.getDescription().contains("123"))
                .verifyComplete();
    }


}
