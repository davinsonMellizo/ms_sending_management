package co.com.bancolombia.model.newness.gateways;

import co.com.bancolombia.model.newness.Newness;
import reactor.core.publisher.Mono;

import java.util.List;

public interface NewnessRepository {
    Mono<Newness> saveNewness(List<Newness> newness);
}
