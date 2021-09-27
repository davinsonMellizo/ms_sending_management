package co.com.bancolombia.usecase.alerttemplate;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.config.model.alerttemplate.AlertTemplate;
import co.com.bancolombia.config.model.alerttemplate.gateways.AlertTemplateGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_TEMPLATE_NOT_FOUND;

@RequiredArgsConstructor
public class AlertTemplateUseCase {

    private final AlertTemplateGateway alertTemplateGateway;

    public Mono<AlertTemplate> saveAlertTemplate(AlertTemplate alertTemplate) {
        return alertTemplateGateway.save(alertTemplate);
    }

    public Mono<AlertTemplate> findAlertTemplateById(Integer id) {
        return alertTemplateGateway.findTemplateById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_TEMPLATE_NOT_FOUND)));
    }

    public Mono<Integer> deleteAlertTemplateById(Integer id) {
        return alertTemplateGateway.findTemplateById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_TEMPLATE_NOT_FOUND)))
                .map(AlertTemplate::getId)
                .flatMap(alertTemplateGateway::delete);
    }
}
