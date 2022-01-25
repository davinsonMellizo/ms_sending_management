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

//    public Mono<TemplateResponse> deleteTemplate(TemplateRequest templateRequest) {
//        return validateTemplate(templateRequest.getIdTemplate())
//                .map(templateResponse -> {
//                    templateRepository.deleteTemplate(templateRequest);
//                    return templateResponse;
//                });
//    }
//
//    public Mono<TemplateResponse> validateTemplate(String idTemplate) {
//        return templateRepository.getTemplate(idTemplate)
//                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
//    }
}
