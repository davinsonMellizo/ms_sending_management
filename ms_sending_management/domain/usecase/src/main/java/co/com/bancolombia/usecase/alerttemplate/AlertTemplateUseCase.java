package co.com.bancolombia.usecase.alerttemplate;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alerttemplate.AlertTemplate;
import co.com.bancolombia.model.alerttemplate.gateways.AlertTemplateGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_TEMPLATE_NOT_FOUND;

@RequiredArgsConstructor
public class AlertTemplateUseCase {

    private final AlertTemplateGateway alertTemplateGateway;

    public Mono<AlertTemplate> saveAlertTemplate(AlertTemplate alertTemplate) {
        return alertTemplateGateway.save(alertTemplate);
    }

    public Mono<List<AlertTemplate>> findAlertTemplateById(String id) {
        return alertTemplateGateway.findTemplateById(id)
                .collectList()
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_TEMPLATE_NOT_FOUND)));
    }

    public Mono<String> deleteAlertTemplateById(String id) {
        return alertTemplateGateway.findTemplateById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_TEMPLATE_NOT_FOUND)))
                .map(AlertTemplate::getId)
                .flatMap(alertTemplateGateway::delete)
                .then(Mono.just(id));
    }
}
