package co.com.bancolombia.usecase.deletetemplate;

import co.com.bancolombia.model.template.gateways.TemplateRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteTemplateUseCase {

    private final TemplateRepository templateRepository;

//    public Mono<Template> deleteTemplate(Template templateRequest) {
//        return validateTemplate(templateRequest.getIdTemplate())
//                .map(templateResponse -> {
//                    templateRepository.deleteTemplate(templateRequest);
//                    return templateResponse;
//                });
//    }
//
//    public Mono<Template> validateTemplate(String idTemplate) {
//        return templateRepository.getTemplate(idTemplate)
//                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
//    }
}
