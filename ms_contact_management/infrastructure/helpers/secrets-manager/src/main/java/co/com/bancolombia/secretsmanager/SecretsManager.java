package co.com.bancolombia.secretsmanager;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.log.LoggerBuilder;
import co.com.bancolombia.secretsmanager.connector.AWSSecretManagerConnectorAsync;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.SECRET_EXCEPTION;

@Component
@RequiredArgsConstructor
public class SecretsManager {

    private final LoggerBuilder logger;
    private final AWSSecretManagerConnectorAsync awsSecretManagerConnector;

    public <T> Mono<T> getSecret(String secret, Class<T> cls) {
        return awsSecretManagerConnector.getSecret(secret, cls)
                .doOnSuccess(e -> logger.info(String.format("Secret %s was obtained successfully", secret)))
                .onErrorMap(error -> {
                    String message = String.join(" ", secret, error.getMessage());
                    return new TechnicalException(message, SECRET_EXCEPTION);
                });
    }

}
