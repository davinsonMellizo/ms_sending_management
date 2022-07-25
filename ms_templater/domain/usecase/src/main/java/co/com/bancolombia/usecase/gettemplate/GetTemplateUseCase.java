package co.com.bancolombia.usecase.gettemplate;

import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.template.dto.Template;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetTemplateUseCase {

    private final TemplateRepository templateRepository;

    public Mono<Template> getTemplate(Template template) {
        return templateRepository.getTemplate(template.getIdTemplate())
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
    }
}
