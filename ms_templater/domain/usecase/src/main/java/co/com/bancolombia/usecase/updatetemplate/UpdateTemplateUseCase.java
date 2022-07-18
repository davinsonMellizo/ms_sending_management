package co.com.bancolombia.usecase.updatetemplate;

import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.template.dto.Template;
import co.com.bancolombia.model.template.dto.UpdateTemplateResponse;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateTemplateUseCase {

    private final TemplateRepository templateRepository;

    public Mono<UpdateTemplateResponse> updateTemplate(Template request) {
        UpdateTemplateResponse updateResponse = UpdateTemplateResponse.builder().build();
        return validateTemplate(request.getIdTemplate())
                .flatMap(response -> {
                    updateResponse.setBefore(response);
                    return templateRepository
                            .saveTemplate(request.toBuilder().creationDate(response.getCreationDate()).build())
                            .map(unused -> request.getIdTemplate());

                })
                .flatMap(this::validateTemplate)
                .map(templateResponse -> {
                    updateResponse.setCurrent(templateResponse);
                    return updateResponse;
                });
    }

    public Mono<Template> validateTemplate(String idTemplate) {
        return templateRepository.getTemplate(idTemplate)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
    }
}
