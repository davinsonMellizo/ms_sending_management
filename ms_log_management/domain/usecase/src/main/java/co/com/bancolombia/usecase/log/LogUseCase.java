package co.com.bancolombia.usecase.log;

import co.com.bancolombia.model.log.Log;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LogUseCase {
    public Mono<Void> saveLog(Log log){
        return Mono.empty();
    }
}
