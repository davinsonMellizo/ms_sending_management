package co.com.bancolombia.model.newness.gateways;

import co.com.bancolombia.model.newness.Newness;
import reactor.core.publisher.Mono;

public interface NewnessRepository {
    Mono<Newness> save(Newness newness);
}
