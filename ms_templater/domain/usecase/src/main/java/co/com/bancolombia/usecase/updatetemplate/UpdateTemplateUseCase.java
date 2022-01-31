package co.com.bancolombia.usecase.updatetemplate;

import co.com.bancolombia.commons.constants.Constants;
import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class UpdateTemplateUseCase {

    private final TemplateRepository templateRepository;

    public Mono<Map<String, TemplateRequest>> updateTemplate(TemplateRequest templateRequest) {
        Map<String, TemplateRequest> templateResponseMap = new HashMap<>();
        return validateTemplate(templateRequest.getIdTemplate())
                .flatMap(templateResponse -> {
                    templateResponseMap.put(Constants.BEFORE, templateResponse);
                    return templateRepository.updateTemplate(templateRequest);
                })
                .flatMap(unused -> validateTemplate(templateRequest.getIdTemplate()))
                .map(templateRequest1 -> {
                    templateResponseMap.put(Constants.ACTUAL, templateRequest1);
                    return templateResponseMap;
                });
    }

    public Mono<TemplateRequest> validateTemplate(String idTemplate) {
        return templateRepository.getTemplate(idTemplate)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
    }
}
