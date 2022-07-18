package co.com.bancolombia.usecase.createtemplate;

import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.template.dto.Template;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateTemplateUseCase {

    private final TemplateRepository templateRepository;

    public Mono<Template> createTemplate(Template template) {
        return validateTemplate(template.getIdTemplate())
                .switchIfEmpty(Mono.defer(() -> templateRepository.saveTemplate(template)));
    }

    public Mono<Template> validateTemplate(String idTemplate) {
        return templateRepository.getTemplate(idTemplate)
                .filter(templateResponse -> !templateResponse.getIdTemplate().isEmpty())
                .flatMap(templateResponse ->
                        Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_ALREADY_EXISTS)));
    }
}
