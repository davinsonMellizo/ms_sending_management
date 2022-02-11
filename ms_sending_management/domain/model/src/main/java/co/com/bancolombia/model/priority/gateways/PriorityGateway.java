package co.com.bancolombia.model.priority.gateways;

import co.com.bancolombia.model.priority.Priority;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PriorityGateway {

    Mono<List<Priority>> findAllByProvider(String idProvider);

    Mono<Priority> findPriorityById(Integer id);

    Mono<Priority> savePriority(Priority priority);

    Mono<StatusResponse<Priority>> updatePriority(Priority priority);

    Mono<Integer> deletePriorityById(Integer id);
}
