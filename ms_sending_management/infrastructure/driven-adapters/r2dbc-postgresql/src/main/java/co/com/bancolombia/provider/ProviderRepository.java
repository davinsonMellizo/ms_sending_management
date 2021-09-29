package co.com.bancolombia.provider;

import co.com.bancolombia.provider.data.ProviderData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProviderRepository extends ReactiveCrudRepository<ProviderData, String> {
}
