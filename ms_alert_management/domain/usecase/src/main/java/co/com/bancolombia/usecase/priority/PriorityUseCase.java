package co.com.bancolombia.usecase.priority;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.priority.Priority;
import co.com.bancolombia.model.priority.gateways.PriorityGateway;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.PRIORITY_NOT_FOUND;

@RequiredArgsConstructor
public class PriorityUseCase {

    private final PriorityGateway priorityGateway;

    public Mono<List<Priority>> findAllByProvider(String idProvider) {
        return priorityGateway.findAllByProvider(idProvider)
                .filter(priorities -> !priorities.isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(PRIORITY_NOT_FOUND)));
    }

    public Mono<Priority> findPriorityById(Integer id) {
        return priorityGateway.findPriorityById(id)
                .switchIfEmpty(Mono.error(new BusinessException(PRIORITY_NOT_FOUND)));
    }

    public Mono<Priority> savePriority(Priority priority) {
        return priorityGateway.savePriority(priority);
    }

    public Mono<StatusResponse<Priority>> updatePriority(Priority priority) {
        return priorityGateway.updatePriority(priority)
                .switchIfEmpty(Mono.error(new BusinessException(PRIORITY_NOT_FOUND)));
    }

    public Mono<Integer> deletePriorityById(Integer id) {
        return priorityGateway.findPriorityById(id)
                .switchIfEmpty(Mono.error(new BusinessException(PRIORITY_NOT_FOUND)))
                .map(Priority::getId)
                .flatMap(priorityGateway::deletePriorityById);
    }
}
