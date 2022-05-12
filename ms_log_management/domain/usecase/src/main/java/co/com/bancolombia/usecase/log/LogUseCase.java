package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.model.log.gateways.LogGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LogUseCase {
    private final LogGateway logGateway;
    public Mono<Void> saveLog(Log log){
        System.out.println("llega");
        return logGateway.saveLog(log)
                .flatMap(logSaved -> Mono.empty());
    }
}
