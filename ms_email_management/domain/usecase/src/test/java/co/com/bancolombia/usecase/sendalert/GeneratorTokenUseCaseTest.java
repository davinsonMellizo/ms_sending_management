package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.binstash.api.ObjectCache;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.DynamoGateway;
import co.com.bancolombia.model.token.Secret;
import co.com.bancolombia.model.token.SecretGateway;
import co.com.bancolombia.model.token.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneratorTokenUseCaseTest {
    @InjectMocks
    private GeneratorTokenUseCase genUseCase;
    @Mock
    private ObjectCache<ArrayList> token;
    @Mock
    private SecretGateway secretGateway;
    @Mock
    private DynamoGateway dynamoGateway;
    @Mock
    private MasivianGateway masivianGateway;

    private Alert alert = new Alert();
    private Mail mail = new Mail();
    private  ArrayList<String> tokens = new ArrayList<>();


    @BeforeEach
    public void init(){

        alert.setProvider("MAS");
        alert.setFrom("pruebas@solicitudesgrupobancolombia.com.co");
        mail.setFrom("pruebas@solicitudesgrupobancolombia.com.co");
        mail.setNameToken("NameTokenTest");
        tokens.add("tokenTest");


    }

    @Test
    void getNameTokenTest() {
        when(dynamoGateway.getTokenName(anyString()))
                .thenReturn(Mono.just(Secret.builder().secretName("NameTokenTest").build()));
        StepVerifier
                .create(genUseCase.getNameToken(alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getTokenWhitTokenCacheTest(){
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .username("usernameTest").password("passwordTest")
                        .build()));
        when(token.get(anyString(),any()))
                .thenReturn(Mono.just(tokens));
        StepVerifier
                .create(genUseCase.getToken(mail))
                .expectNextCount(1)
                .verifyComplete();
    }
    @Test
    void getTokenWhitOutTokenCacheTest(){
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .username("usernameTest").password("passwordTest")
                        .build()));
        when(token.get(anyString(),any()))
                .thenReturn(Mono.empty());
        when(masivianGateway.getToken(any()))
                .thenReturn(Mono.just(Token.builder()
                        .accessToken("accesTokenTest").build()));
        when(token.save(anyString(),any()))
                .thenReturn(Mono.just(tokens));
        StepVerifier
                .create(genUseCase.getToken(mail))
                .expectNextCount(1)
                .verifyComplete();
    }
    @Test
    void getTokenWhitTokenCacheErrorTest(){
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .username("usernameTest").password("passwordTest")
                        .build()));
        when(token.get(anyString(),any()))
                .thenReturn(Mono.error(new Throwable("error")));
        StepVerifier
                .create(genUseCase.getToken(mail))
                .expectError()
                .verify();
    }
    @Test
    void getTokenWhitOutTokenCacheErrorTest(){
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .username("usernameTest").password("passwordTest")
                        .build()));
        when(token.get(anyString(),any()))
                .thenReturn(Mono.empty());
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.error(new Throwable("error")));
        StepVerifier
                .create(genUseCase.getToken(mail))
                .expectError()
                .verify();
    }

    @Test
    void deleteTokenTest() {
        when(dynamoGateway.getTokenName(anyString()))
                .thenReturn(Mono.just(Secret.builder().secretName("NameTokenTest").build()));
        when(token.get(anyString(),any()))
                .thenReturn(Mono.just(tokens));
        when(token.save(anyString(),any()))
                .thenReturn(Mono.empty());
        StepVerifier
                .create(genUseCase.deleteToken("tokenTestForDeleted",alert))
                .verifyComplete();
    }
}
