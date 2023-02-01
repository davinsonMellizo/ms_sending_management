package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestClient;
import co.com.bancolombia.consumer.adapter.response.Error;
import co.com.bancolombia.consumer.adapter.response.ErrorTokenRefreshInalambria;
import co.com.bancolombia.consumer.adapter.response.model.TokenMasivData;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.token.Account;
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
class MasivianTokenAdapterTest {
    @InjectMocks
    private MasivAdapter masivAdapter;

    @Mock
    private RestClient<TokenMasivData, TokenMasivData> clientToken;
    @Mock
    private ConsumerProperties properties;

    private Account account = new Account();

    @BeforeEach
    public void init() {
        String url = "localhost";
        when(properties.getResources())
                .thenReturn(new ConsumerProperties.Resources(url, url, url, url, url, url, url , url));

        account.setPassword("passwordTest");
        account.setUsername("usernameTest");
    }

    @Test
    void getTokenTest() {
        when(clientToken.post(anyString(), any(), any(), any()))
                .thenReturn(Mono.just(TokenMasivData.builder()
                        .accesToken("accessTokenTest").expiresIn(1234L).build()));
        StepVerifier
                .create(masivAdapter.getToken(account))
                .assertNext(response -> response.getAccessToken().equals("accessTokenTest"))
                .verifyComplete();
    }

    @Test
    void getTokenErrorTest() {
        when(clientToken.post(anyString(), any(), any(), any()))
                .thenReturn(Mono.error(Error.builder()
                        .httpsStatus(400)
                        .data(new ErrorTokenRefreshInalambria())
                        .build()));
        StepVerifier.create(masivAdapter.getToken(account))
                .expectError()
                .verify();
    }

}
