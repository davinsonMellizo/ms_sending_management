package co.com.bancolombia.secretsmanager;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.log.LoggerBuilder;
import co.com.bancolombia.logging.technical.logger.TechLogger;
import connector.AWSSecretManagerConnector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;
import static reactor.core.publisher.Mono.just;

@ExtendWith(MockitoExtension.class)
public class SecretsManagerTest {

    public static final String SECRET = "any-secret-dev";

    @InjectMocks
    private SecretsManager secretsManager;

    @Mock
    private AWSSecretManagerConnector secretsConnector;

    @Mock
    private LoggerBuilder loggerBuilder;

    @Mock
    protected TechLogger techLogger;

    @BeforeTestMethod
    public void init() {
        ReflectionTestUtils.setField(loggerBuilder, "logger", techLogger);
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
                .expectErrorMatches(e -> e instanceof TechnicalException);
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
