package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.binstash.api.ObjectCache;
import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import co.com.bancolombia.model.message.gateways.InfobipGateway;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.token.Account;
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
import java.util.UUID;

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
    private InalambriaGateway inalambriaGateway;
    @Mock
    private MasivianGateway masivianGateway;
    @Mock
    private InfobipGateway infobipGateway;

    private Alert alert = new Alert();
    private SMSInalambria smsIna = new SMSInalambria();
    private SMSMasiv smsMas = new SMSMasiv();

    private SMSInfobip smsInf = new SMSInfobip();
    private ArrayList<String> tokens = new ArrayList<>();

    @BeforeEach
    public void init() {
        alert.setTo(Alert.To.builder().phoneNumber("number").phoneIndicator("123").build());
        alert.setUrlForShortening("");
        alert.setProvider("INA");
        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("name", "bancolombia", ""));
        alert.setMessage("text to send");
        alert.setLogKey(UUID.randomUUID().toString());
        alert.setPriority("1");

        tokens.add("tokenTest");

    }

    @Test
    void getTokenInaWhitTokenCacheTest() {
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .password("passwordTest").username("usernameTest")
                        .build()));
        when(token.get(anyString(), any()))
                .thenReturn(Mono.just(tokens));

        StepVerifier
                .create(genUseCase.getTokenINA(smsIna, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getTokenMasWhitTokenCacheTest() {
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .password("passwordTest").username("usernameTest")
                        .build()));
        alert.setProvider("MAS");
        when(token.get(anyString(), any()))
                .thenReturn(Mono.just(tokens));
        StepVerifier
                .create(genUseCase.getTokenMAS(smsMas, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getTokenInfWhitTokenCacheTest() {
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .password("passwordTest").username("usernameTest")
                        .build()));
        alert.setProvider("INF");
        when(token.get(anyString(), any()))
                .thenReturn(Mono.just(tokens));
        StepVerifier
                .create(genUseCase.getTokenInf(smsInf, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getTokenInaWhitOutTokenCacheTest() {
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .password("passwordTest").username("usernameTest")
                        .build()));
        when(token.get(anyString(), any()))
                .thenReturn(Mono.empty());
        when(inalambriaGateway.getToken(any())).thenReturn(Mono.just(Token.builder()
                .accessToken("accessTokenTest").tokenType("tokenType").refreshToken("refreshToken")
                .expiresIn(12345L).build()));
        when(token.save(anyString(), any()))
                .thenReturn(Mono.just(tokens));
        StepVerifier
                .create(genUseCase.getTokenINA(smsIna, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getTokenMasWhitOutTokenCacheTest() {
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .password("passwordTest").username("usernameTest")
                        .build()));
        alert.setProvider("MAS");
        when(token.get(anyString(), any()))
                .thenReturn(Mono.empty());
        when(masivianGateway.getToken(any())).thenReturn(Mono.just(Token.builder()
                .accessToken("accessToken").tokenType("tokenType").refreshToken("refreshToken")
                .expiresIn(12345L).build()));
        when(token.save(anyString(), any()))
                .thenReturn(Mono.just(tokens));
        StepVerifier
                .create(genUseCase.getTokenMAS(smsMas, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getTokenInfWhitOutTokenCacheTest() {
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .password("passwordTest").username("usernameTest")
                        .build()));
        alert.setProvider("INF");
        when(token.get(anyString(), any()))
                .thenReturn(Mono.empty());
        when(infobipGateway.getToken(any())).thenReturn(Mono.just(Token.builder()
                .accessToken("accessToken").tokenType("tokenType").refreshToken("refreshToken")
                .expiresIn(12345L).build()));
        when(token.save(anyString(), any()))
                .thenReturn(Mono.just(tokens));
        StepVerifier
                .create(genUseCase.getTokenInf(smsInf, alert))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getTokenInaWhitTokenCacheErrorTest() {
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .password("passwordTest").username("usernameTest")
                        .build()));
        when(token.get(anyString(), any()))
                .thenReturn(Mono.error(new Throwable("error")));
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.error(new Throwable("error")));
        StepVerifier
                .create(genUseCase.getTokenINA(smsIna, alert))
                .expectError()
                .verify();
    }

    @Test
    void getTokenMasWhitTokenCacheErrorTest() {
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .password("passwordTest").username("usernameTest")
                        .build()));
        alert.setProvider("MAS");
        when(token.get(anyString(), any()))
                .thenReturn(Mono.error(new Throwable("error")));
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.error(new Throwable("error")));
        StepVerifier
                .create(genUseCase.getTokenMAS(smsMas, alert))
                .expectError()
                .verify();
    }

    @Test
    void getTokenInfWhitTokenCacheErrorTest() {
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .password("passwordTest").username("usernameTest")
                        .build()));
        alert.setProvider("INF");
        when(token.get(anyString(), any()))
                .thenReturn(Mono.error(new Throwable("error")));
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.error(new Throwable("error")));
        StepVerifier
                .create(genUseCase.getTokenInf(smsInf, alert))
                .expectError()
                .verify();
    }


    @Test
    void getTokenInaWhitOutTokenCacheErrorTest() {
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .password("passwordTest").username("usernameTest")
                        .build()));
        when(token.get(anyString(), any()))
                .thenReturn(Mono.empty());
        when(inalambriaGateway.getToken(any())).thenReturn(Mono.just(Token.builder()
                .accessToken("accessToken").tokenType("tokenType").refreshToken("refreshToken")
                .expiresIn(12345L).build()));
        when(token.save(anyString(), any()))
                .thenReturn(Mono.error(new Throwable("error")));
        StepVerifier
                .create(genUseCase.getTokenINA(smsIna, alert))
                .expectError()
                .verify();
    }

    @Test
    void getTokenMasWhitOutTokenCacheErrorTest() {
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .password("passwordTest").username("usernameTest")
                        .build()));
        alert.setProvider("MAS");
        when(token.get(anyString(), any()))
                .thenReturn(Mono.empty());
        when(masivianGateway.getToken(any())).thenReturn(Mono.just(Token.builder()
                .accessToken("accessToken").tokenType("tokenType").refreshToken("refreshToken")
                .expiresIn(12345L).build()));
        when(token.save(anyString(), any()))
                .thenReturn(Mono.error(new Throwable("error")));
        StepVerifier
                .create(genUseCase.getTokenMAS(smsMas, alert))
                .expectError()
                .verify();
    }

    @Test
    void getTokenInfWhitOutTokenCacheErrorTest() {
        when(secretGateway.getSecretName(anyString()))
                .thenReturn(Mono.just(Account.builder()
                        .password("passwordTest").username("usernameTest")
                        .build()));
        alert.setProvider("INF");
        when(token.get(anyString(), any()))
                .thenReturn(Mono.empty());
        when(infobipGateway.getToken(any())).thenReturn(Mono.just(Token.builder()
                .accessToken("accessToken").tokenType("tokenType").refreshToken("refreshToken")
                .expiresIn(12345L).build()));
        when(token.save(anyString(), any()))
                .thenReturn(Mono.error(new Throwable("error")));
        StepVerifier
                .create(genUseCase.getTokenInf(smsInf, alert))
                .expectError()
                .verify();
    }

    @Test
    void deleteTokenTest() {
        when(token.get(anyString(), any()))
                .thenReturn(Mono.just(tokens));
        when(token.save(anyString(), any()))
                .thenReturn(Mono.empty());
        StepVerifier
                .create(genUseCase.deleteToken(tokens.get(0), alert))
                .verifyComplete();
    }
}
