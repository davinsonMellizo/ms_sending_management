package co.com.bancolombia.injector;

import co.com.bancolombia.d2b.model.oauth.GenericClientPassword;
import co.com.bancolombia.d2b.model.secret.AsyncSecretVault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.ExchangeFunctions;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApicAuthFilterFunctionTest {


    @InjectMocks
    private ApicAuthFilterFunction apicAuthFilterFunction;

    @Mock
    private AsyncSecretVault asyncSecretVault;

    private ClientRequest clientRequest;

    private ExchangeFunction exchangeFunction;
    private String url = "http://example.com";

    @BeforeEach
    void init() throws URISyntaxException {
        clientRequest = ClientRequest.create(HttpMethod.GET, new URI(url)).build();
        exchangeFunction = ExchangeFunctions.create(new ReactorClientHttpConnector());

    }


    @Test
    void apicCredsFilterFunctionTest(){
       apicAuthFilterFunction = new ApicAuthFilterFunction(asyncSecretVault, "secretName");
       when(asyncSecretVault.getSecret("secretName", GenericClientPassword.class))
               .thenReturn(Mono.just(GenericClientPassword.builder().clientId("clientId").clientSecret("clientSecret").build()));
       apicAuthFilterFunction.filter(clientRequest, exchangeFunction)
               .flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
               .as(StepVerifier::create)
               .consumeNextWith(body -> body.contains(url))
               .verifyComplete();
    }

    @Test
    void apicCredsFilterFunctionErrorTest(){
        apicAuthFilterFunction = new ApicAuthFilterFunction(asyncSecretVault, "secretName");
        when(asyncSecretVault.getSecret("secretName", GenericClientPassword.class))
                .thenReturn(Mono.error(new Throwable("not fuond")));
        StepVerifier.create(apicAuthFilterFunction.filter(clientRequest, exchangeFunction))
                .expectError(Throwable.class)
                .verify();
    }
}
