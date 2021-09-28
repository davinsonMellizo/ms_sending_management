package co.com.bancolombia.model.service.gateways;

import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.service.Service;
import reactor.core.publisher.Mono;

public interface ServiceGateway {
    Mono<Service> findServiceById(Integer id);

    Mono<Service> saveService(Service service);

    Mono<StatusResponse<Service>> updateService(Service service);

    Mono<Integer> deleteServiceById(Integer id);
}
