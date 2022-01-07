package co.com.bancolombia.usecase.gettemplate;

import co.com.bancolombia.commons.constants.Constants;
import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.template.dto.TemplateResponse;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GetTemplateUseCase {

    private final TemplateRepository templateRepository;

    public Mono<List<TemplateResponse>> getTemplate(Map<String, String> header) {
        List<TemplateResponse> templateResponses = new ArrayList<>();
        return templateRepository.getTemplate(header.get(Constants.ID), header.get(Constants.MESSAGE_TYPE),
                header.get(Constants.MESSAGE_SUBJECT))
                .collectList()
                .map(templates -> {
                    if (templates.isEmpty()) {
                        throw new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND);
                    }
                    templateResponses.addAll(templates);
                    return templateResponses;
                });
    }
}
