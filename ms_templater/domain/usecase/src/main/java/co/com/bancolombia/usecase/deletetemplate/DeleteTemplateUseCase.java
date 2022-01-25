package co.com.bancolombia.usecase.deletetemplate;

import co.com.bancolombia.commons.constants.Constants;
import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteTemplateUseCase {

    private final TemplateRepository templateRepository;

    public Mono<TemplateResponse> deleteTemplate(TemplateRequest templateRequest) {
        return validateTemplate(templateRequest.getIdTemplate())
                .collectList()
                .map(templateResponses -> {
                    templateRepository.deleteTemplate(templateRequest);
                    return templateResponses.get(Constants.ZERO);
                });
    }

    public Flux<TemplateResponse> validateTemplate(String idTemplate) {
        return templateRepository.getTemplate(idTemplate, null, null)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
    }
}
