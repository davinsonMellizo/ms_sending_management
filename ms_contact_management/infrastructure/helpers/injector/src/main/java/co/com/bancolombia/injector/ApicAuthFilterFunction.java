package co.com.bancolombia.injector;

import co.com.bancolombia.d2b.model.oauth.GenericClientPassword;
import co.com.bancolombia.d2b.model.secret.AsyncSecretVault;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Log4j2
@Getter
@RequiredArgsConstructor
public class ApicAuthFilterFunction implements ExchangeFilterFunction {

    private static final String CLIENT_ID_HEADER_NAME = "Client-Id";
    private static final String CLIENT_SECRET_HEADER_NAME = "Client-Secret";

    private final AsyncSecretVault asyncSecretVault;
    private final String secretName;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return doRequestSecret()
                .flatMap(genericClientPassword -> {
                    ClientRequest newRequest = ClientRequest.from(request)
                            .header(CLIENT_ID_HEADER_NAME, genericClientPassword.getClientId())
                            .header(CLIENT_SECRET_HEADER_NAME, genericClientPassword.getClientSecret())
                            .build();
                    return next.exchange(newRequest);
                });
    }

    private Mono<GenericClientPassword> doRequestSecret() {
        return asyncSecretVault.getSecret(secretName, GenericClientPassword.class)
                .onErrorMap(throwable -> {
                    log.error("Secret '{}', for apic credentials could not be fetched. Error: ", secretName, throwable);
                    return throwable;
                });
    }
}
