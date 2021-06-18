package co.com.bancolombia.service;


import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.service.Service;
import co.com.bancolombia.model.service.gateways.ServiceGateway;
import co.com.bancolombia.service.data.ServiceData;
import co.com.bancolombia.service.data.ServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class ServiceRepositoryImplement
        extends AdapterOperations<Service, ServiceData, Integer, ServiceRepository>
        implements ServiceGateway {

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    public ServiceRepositoryImplement(ServiceRepository repository, ServiceMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }


    @Override
    public Mono<Service> findServiceById(Integer id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_SERVICE_BY_ID_ERROR));
    }

    @Override
    public Mono<Service> saveService(Service service) {
        return Mono.just(service)
                .map(this::convertToData)
                .map(serviceData -> serviceData.toBuilder()
                        .isNew(true)
                        .createdDate(timeFactory.now())
                        .build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_SERVICE_ERROR));
    }

    @Override
    public Mono<StatusResponse<Service>> updateService(Service service) {
        return findServiceById(service.getId())
                .map(serviceFound  -> StatusResponse.<Service>builder()
                        .before(serviceFound)
                        .actual(service)
                        .build())
                .flatMap(this::update);
    }

    private Mono<StatusResponse<Service>> update(StatusResponse<Service> statusResponse) {
        return Mono.just(statusResponse.getBefore())
                .map(this::convertToData)
                .map(data -> data.toBuilder().isNew(false).name(statusResponse.getActual().getName()).build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .map(actual -> statusResponse.toBuilder().actual(actual).description("Actualización Exitosa").build())
                .onErrorMap(e -> new TechnicalException(e, UPDATE_SERVICE_ERROR));
    }

    @Override
    public Mono<Integer> deleteServiceById(Integer id) {
        return repository.deleteById(id)
                .onErrorMap(e -> new TechnicalException(e, DELETE_SERVICE_ERROR))
                .thenReturn(id);
    }
}
