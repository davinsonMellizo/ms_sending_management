package co.com.bancolombia.model.remitter.gateways;


import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

public interface RemitterGateway {
    Mono<Remitter> findRemitterById(Integer id);
    Mono<Remitter> saveRemitter(Remitter Remitter);
    Mono<StatusResponse<Remitter>> updateRemitter(Remitter Remitter);
    Mono<Integer> deleteRemitterById(Integer id);
}
