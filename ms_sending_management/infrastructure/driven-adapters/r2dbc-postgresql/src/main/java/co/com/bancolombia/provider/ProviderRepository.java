package co.com.bancolombia.provider;

import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.provider.data.ProviderData;
import co.com.bancolombia.remitter.data.RemitterData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProviderRepository extends ReactiveCrudRepository<ProviderData, String> {
    @Query("INSERT INTO provider " +
            "(id, name, type_service, creation_user, created_date) " +
            "values" +
            "('DAV', 'DAVIN', 'E', 'user', '2021-02-16 10:10:25-05')")
    Mono<Provider> insert();

}
