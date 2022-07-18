package co.com.bancolombia.model.template.gateways;

import co.com.bancolombia.model.template.dto.Template;
import reactor.core.publisher.Mono;

public interface TemplateRepository {
    Mono<Template> getTemplate(String idTemplate);
    Mono<Template> saveTemplate(Template template);
    Mono<Template> deleteTemplate(Template template);
}
