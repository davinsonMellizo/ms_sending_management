package co.com.bancolombia.usecase.service;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.config.model.response.StatusResponse;
import co.com.bancolombia.config.model.service.Service;
import co.com.bancolombia.config.model.service.gateways.ServiceGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.SERVICE_NOT_FOUND;

@RequiredArgsConstructor
public class ServiceUseCase {
    private final ServiceGateway serviceGateway;

    public Mono<Service> findServiceById(Integer id) {
        return serviceGateway.findServiceById(id)
                .switchIfEmpty(Mono.error(new BusinessException(SERVICE_NOT_FOUND)));
    }

    public Mono<Service> saveService(Service service) {
        return serviceGateway.saveService(service);
    }

    public Mono<StatusResponse<Service>> updateService(Service service) {
        return serviceGateway.updateService(service)
                .switchIfEmpty(Mono.error(new BusinessException(SERVICE_NOT_FOUND)));
    }

    public Mono<Integer> deleteServiceById(Integer id) {
        return serviceGateway.findServiceById(id)
                .switchIfEmpty(Mono.error(new BusinessException(SERVICE_NOT_FOUND)))
                .map(Service::getId)
                .flatMap(serviceGateway::deleteServiceById);
    }
}
