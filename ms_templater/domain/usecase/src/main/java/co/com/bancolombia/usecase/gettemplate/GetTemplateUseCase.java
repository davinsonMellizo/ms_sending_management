package co.com.bancolombia.usecase.gettemplate;

import co.com.bancolombia.commons.constants.Constants;
import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.template.dto.TemplateResponse;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
public class GetTemplateUseCase {

    private final TemplateRepository templateRepository;

    public Mono<TemplateResponse> getTemplate(Map<String, String> header) {
        return templateRepository.getTemplate(header.get(Constants.ID_TEMPLATE))
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
    }
}
