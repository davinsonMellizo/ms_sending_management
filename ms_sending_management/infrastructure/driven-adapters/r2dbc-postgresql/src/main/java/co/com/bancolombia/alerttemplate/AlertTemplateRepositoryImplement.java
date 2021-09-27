package co.com.bancolombia.alerttemplate;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.alerttemplate.data.AlertTemplateData;
import co.com.bancolombia.alerttemplate.data.AlertTemplateMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.config.model.alerttemplate.AlertTemplate;
import co.com.bancolombia.config.model.alerttemplate.gateways.AlertTemplateGateway;
import co.com.bancolombia.drivenadapters.TimeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class AlertTemplateRepositoryImplement
        extends AdapterOperations<AlertTemplate, AlertTemplateData, Integer, AlertTemplateRepository>
        implements AlertTemplateGateway {

    @Autowired
    private TimeFactory timeFactory;

    public AlertTemplateRepositoryImplement(AlertTemplateRepository repository, AlertTemplateMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Override
    public Mono<AlertTemplate> save(AlertTemplate alertTemplate) {
        return Mono.just(alertTemplate)
                .map(this::convertToData)
                .map(alertTemp -> alertTemp.toBuilder()
                        .isNew(true)
                        .createdDate(timeFactory.now())
                        .build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_ALERT_TEMPLATE_ERROR));
    }

    @Override
    public Mono<AlertTemplate> findTemplateById(Integer id) {
        return doQuery(repository.findById(id))
                .onErrorMap(e -> new TechnicalException(e, FIND_ALERT_TEMPLATE_BY_ID_ERROR));
    }

    @Override
    public Mono<Integer> delete(Integer id) {
        return repository.deleteById(id)
                .onErrorMap(e -> new TechnicalException(e, DELETE_ALERT_TEMPLATE_ERROR))
                .thenReturn(id);
    }
}
