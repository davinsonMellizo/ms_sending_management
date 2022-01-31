package co.com.bancolombia.model.template.gateways;

import co.com.bancolombia.model.template.dto.TemplateRequest;
import reactor.core.publisher.Mono;

public interface TemplateRepository {
    Mono<TemplateRequest> getTemplate(String idTemplate);

    Mono<TemplateRequest> createTemplate(TemplateRequest templateRequest);

    Mono<TemplateRequest> updateTemplate(TemplateRequest templateRequest);
    /*  Mono<TemplateResponse> deleteTemplate(TemplateRequest templateRequest);*/
}
