package co.com.bancolombia.service;

import co.com.bancolombia.service.data.ServiceData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ServiceRepository extends ReactiveCrudRepository<ServiceData, Integer> {

}
