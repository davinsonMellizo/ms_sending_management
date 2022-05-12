package co.com.bancolombia.provider;

import co.com.bancolombia.provider.data.ProviderData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProviderRepository extends ReactiveCrudRepository<ProviderData, String> {

    @Query("select p.* from provider_service s inner join " +
            "provider p on s.id_provider = p.id " +
            "where s.id = $1")
    Mono<ProviderData> finProvider(Integer id);
}
