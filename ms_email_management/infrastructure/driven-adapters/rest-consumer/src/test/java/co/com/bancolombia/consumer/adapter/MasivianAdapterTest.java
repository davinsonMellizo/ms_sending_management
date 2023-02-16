package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.ErrorMasivianMAIL;
import co.com.bancolombia.consumer.adapter.response.SuccessMasivianMAIL;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.Mail;
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
    private MasivianAdapter masivianAdapter;

    @Mock
    private ConsumerProperties properties;
    @Mock
    private RestClient<Mail, SuccessMasivianMAIL> client;


    @BeforeEach
    public void init(){
        String url = "localhost";
        when(properties.getResources())
                .thenReturn(new ConsumerProperties.Resources(url,url,url));

    }

    @Test
    void sendMAILSuccessTest(){
        when(client.post(anyString(), any(), any(),any()))
                .thenReturn(Mono.just(SuccessMasivianMAIL.builder()
                        .description("success")
                        .build()));
        StepVerifier.create(masivianAdapter.sendMAIL(new Mail()))
                .assertNext(response -> response.getDescription().equals("success"))
                .verifyComplete();
    }

    @Test
    void sendMAILErrorProviderTest(){
        when(client.post(anyString(), any(), any(),any()))
                .thenReturn(Mono.error(Error.builder()
                        .httpsStatus(400)
                        .data(new ErrorMasivianMAIL("error", "error authentication","data error"))
                        .build()));
        StepVerifier.create(masivianAdapter.sendMAIL(new Mail()))
                .assertNext(response -> response.getDescription().equals("error authentication"))
                .verifyComplete();
    }

    @Test
    void sendMAILErrorWebClientTest(){
        when(client.post(anyString(), any(), any(),any()))
                .thenReturn(Mono.error(new Throwable("123timeout")));
        StepVerifier.create(masivianAdapter.sendMAIL(new Mail()))
                .assertNext(response -> response.getCode().equals(123))
                .verifyComplete();
    }


}
