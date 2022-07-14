package co.com.bancolombia.model.template.gateways;

import co.com.bancolombia.model.template.dto.MessageRequest;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;
import reactor.core.publisher.Mono;

public interface TemplateRepository {
    Mono<TemplateResponse> getTemplate(String idTemplate);
    Mono<TemplateResponse> createTemplate(TemplateRequest templateRequest);
    Mono<TemplateResponse> updateTemplate(TemplateRequest templateRequest);
    Mono<TemplateRequest> deleteTemplate(TemplateRequest templateRequest);
}
