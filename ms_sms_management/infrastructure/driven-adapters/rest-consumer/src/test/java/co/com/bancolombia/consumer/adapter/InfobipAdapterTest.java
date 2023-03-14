package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.RestClientForm;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.ErrorInfobipSMS;
import co.com.bancolombia.consumer.adapter.response.ErrorTokenInfobipRequest;
import co.com.bancolombia.consumer.adapter.response.SuccessInfobipSMS;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.message.SMSInfobip;
import co.com.bancolombia.model.token.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InfobipAdapterTest {

    @InjectMocks
    private InfobipAdapter infobipAdapter;

    @Mock
    private ConsumerProperties properties;
    @Mock
    private RestClient<SMSInfobip, SuccessInfobipSMS> client;

    @Mock
    private RestClientForm<SMSInfobip,ErrorTokenInfobipRequest > clientToken;

    @Mock
    private Account account;


    @BeforeEach
    public void init() {
        String url = "localhost";
        when(properties.getResources()).thenReturn(new ConsumerProperties.Resources(url, url, url, url, url, url, url,
                url,url));
    }

    @Test
    void sendSmsInfobipSuccessTest() {
        when(client.post(anyString(), any(), any(), any()))
                .thenReturn(Mono.just(SuccessInfobipSMS.builder()
                        .messages(Arrays.asList(
                                SMSInfobip.Response.builder().status(SMSInfobip.Status.builder().description("Message sent to next instance").build()).build()))
                        .build()));
        StepVerifier.create(infobipAdapter.sendSMS(new SMSInfobip()))
                .assertNext(response -> response.getMessages().equals(Arrays.asList(
                                SMSInfobip.Response.builder().status(SMSInfobip.Status.builder().description("Message sent to next instance").build()).build()) ))
                .verifyComplete();
    }

    @Test
    void sendSmsErrorInfobipTest() {
        when(client.post(anyString(), any(), any(), any()))
                .thenReturn(Mono.error(Error.builder()
                        .httpsStatus(500)
                        .data(new ErrorInfobipSMS
                                (SMSInfobip.RequestError.builder().serviceException
                                        (SMSInfobip.ServiceException.builder().text("Invalid login details")
                                                .build()).build()))
                        .build()));
        StepVerifier.create(infobipAdapter.sendSMS(new SMSInfobip()))
                .expectError()
                .verify();
    }

    @Test
    void sendSmsErrorTokenInfobipTest() {
        when(account.getUsername()).thenReturn("aso");
        when(account.getPassword()).thenReturn("123");
        when(clientToken.post(anyString(), any(), any(), any()))
                .thenReturn(Mono.error(Error.builder()
                        .httpsStatus(401)
                        .data(new ErrorTokenInfobipRequest
                                ("Access denied",SMSInfobip.RequestError.builder().serviceException
                                        (SMSInfobip.ServiceException.builder().text("Access denied")
                                                .build()).build()))
                        .build()));
        StepVerifier.create(infobipAdapter.getToken(account))
                .expectError()
                .verify();
    }

    @Test
    void sendSmsErrorWebClientTest() {
        when(client.post(anyString(), any(), any(), any()))
                .thenReturn(Mono.error(new Throwable("123timeout")));
        StepVerifier.create(infobipAdapter.sendSMS(new SMSInfobip()))
                .expectError()
                .verify();
    }


}
