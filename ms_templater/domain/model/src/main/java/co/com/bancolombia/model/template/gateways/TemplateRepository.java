package co.com.bancolombia.model.template.gateways;

import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TemplateRepository {
    Flux<TemplateResponse> getTemplate(String idTemplate, String messageType, String messageSubject);
    Mono<TemplateResponse> createTemplate(TemplateRequest templateRequest);
    Mono<TemplateResponse> updateTemplate(TemplateRequest templateRequest);
    Mono<TemplateResponse> deleteTemplate(TemplateRequest templateRequest);
}
