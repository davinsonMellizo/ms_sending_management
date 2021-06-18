package co.com.bancolombia.usecase.remitter;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.REMITTER_NOT_FOUND;

@RequiredArgsConstructor
public class RemitterUseCase {
    private final RemitterGateway remitterGateway;

    public Mono<Remitter> findRemitterById(Integer id){
        return remitterGateway.findRemitterById(id)
                .switchIfEmpty(Mono.error(new BusinessException(REMITTER_NOT_FOUND)));
    }

    public Mono<Remitter> saveRemitter(Remitter remitter){
        return remitterGateway.saveRemitter(remitter);
    }

    public Mono<StatusResponse<Remitter>> updateRemitter(Remitter remitter){
        return remitterGateway.updateRemitter(remitter)
                .switchIfEmpty(Mono.error(new BusinessException(REMITTER_NOT_FOUND)));
    }

    public Mono<Integer> deleteRemitterById(Integer id){
        return remitterGateway.findRemitterById(id)
                .switchIfEmpty(Mono.error(new BusinessException(REMITTER_NOT_FOUND)))
                .map(Remitter::getId)
                .flatMap(remitterGateway::deleteRemitterById);
    }
}
