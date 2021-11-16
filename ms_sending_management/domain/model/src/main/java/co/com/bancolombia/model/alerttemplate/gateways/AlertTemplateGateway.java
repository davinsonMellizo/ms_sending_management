package co.com.bancolombia.model.alerttemplate.gateways;

import co.com.bancolombia.model.alerttemplate.AlertTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlertTemplateGateway {

    Mono<AlertTemplate> save(AlertTemplate alertTemplate);

    Flux<AlertTemplate> findTemplateById(String id);

    Mono<String> delete(String id);

}
