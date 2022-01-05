package co.com.bancolombia.secretsmanager;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.secretsmanager.connector.AWSSecretManagerConnectorAsync;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;
import static reactor.core.publisher.Mono.just;

public class SecretsManagerTest {

    public static final String SECRET = "any-secret-dev";

    @InjectMocks
    private SecretsManager secretsManager;

    @Mock
    private AWSSecretManagerConnectorAsync secretsConnector;

    @Mock
    private LoggerBuilder loggerBuilder;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getSecretWhenExistSecretThenReturnObject() {
        when(secretsConnector.getSecret(SECRET, ClassTest.class))
                .thenReturn(just(ClassTest.builder()
                        .host("localhost")
                        .build()));
        StepVerifier.create(secretsManager.getSecret(SECRET, ClassTest.class))
                .expectNextMatches(classTest -> classTest.getHost().equals("localhost"))
                .verifyComplete();
    }

    @Test
    public void getSecretWhenExistSecretThenReturnError() {
        when(secretsConnector.getSecret(SECRET, ClassTest.class))
                .thenReturn(Mono.error(new RuntimeException("error")));
        StepVerifier.create(secretsManager.getSecret(SECRET, ClassTest.class))
                .expectError(TechnicalException.class)
                .verify();
    }

    @Data
    @Builder
    @AllArgsConstructor
    private static class ClassTest {
        private String dbname;
        private String schema;
        private String username;
        private String password;
        private String host;
        private Integer port;
    }

}
