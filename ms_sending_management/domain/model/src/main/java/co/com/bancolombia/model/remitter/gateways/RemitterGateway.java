package co.com.bancolombia.model.remitter.gateways;


import co.com.bancolombia.model.remitter.Remitter;
import reactor.core.publisher.Mono;

public interface RemitterGateway {
    Mono<Remitter> findRemitterById(Integer id);
}
