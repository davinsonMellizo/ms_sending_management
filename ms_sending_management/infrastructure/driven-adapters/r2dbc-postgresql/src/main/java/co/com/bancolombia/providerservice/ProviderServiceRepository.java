package co.com.bancolombia.providerservice;

import co.com.bancolombia.providerservice.data.ProviderServiceData;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProviderServiceRepository extends ReactiveCrudRepository<ProviderServiceData, Integer> {

    @Query("SELECT ps.*, p.id as code, p.name as description, s.name as service " +
            "FROM schalertd.provider_service ps join provider p on ps.id_provider = p.id " +
            "join service s on ps.id_service = s.id")
    Flux<ProviderServiceData> findAllProviderService();

    @Modifying
    @Query("DELETE FROM provider_service where id_provider = $1 and id_service = $2")
    Mono<Integer> deleteProviderService(String idProvider, Integer idService);
}
