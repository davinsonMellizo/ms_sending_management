package co.com.bancolombia.prefix;

import co.com.bancolombia.prefix.data.PrefixData;
import co.com.bancolombia.service.data.ServiceData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IPrefixRepository extends ReactiveCrudRepository<PrefixData, String> {

}
