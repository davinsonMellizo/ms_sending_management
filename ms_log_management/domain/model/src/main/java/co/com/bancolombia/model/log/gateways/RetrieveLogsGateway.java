package co.com.bancolombia.model.log.gateways;

import co.com.bancolombia.model.log.Filters;
import reactor.core.publisher.Mono;

public interface RetrieveLogsGateway {
    Mono<String> retrieveLogsS3(Filters filters);
}
