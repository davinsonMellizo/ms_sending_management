package co.com.bancolombia.usecase.updatetemplate;

import co.com.bancolombia.commons.constants.Constants;
import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.dto.TemplateResponse;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class UpdateTemplateUseCase {

    private final TemplateRepository templateRepository;

    public Mono<Map<String, TemplateResponse>> updateTemplate(TemplateRequest templateRequest) {
        Map<String, TemplateResponse> templateResponseMap = new HashMap<>();
        return validateTemplate(templateRequest.getId())
                .collectList()
                .flatMap(templateResponses -> {
                    templateResponseMap.put(Constants.BEFORE, templateResponses.get(Constants.ZERO));
                    return templateRepository.updateTemplate(templateRequest);
                })
                .map(templateResponse -> {
                    templateResponseMap.put(Constants.ACTUAL, templateResponse);
                    return templateResponseMap;
                });
    }

    public Flux<TemplateResponse> validateTemplate(String id) {
        return templateRepository.getTemplate(id, null, null)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
    }
}
