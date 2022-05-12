package co.com.bancolombia.model.priority.gateways;

import co.com.bancolombia.model.priority.Priority;
import reactor.core.publisher.Mono;

public interface PriorityGateway {
    Mono<Priority> findPriorityById(Integer id);
}
