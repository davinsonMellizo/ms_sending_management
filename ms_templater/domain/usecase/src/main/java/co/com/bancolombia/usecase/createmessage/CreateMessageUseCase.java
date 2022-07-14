package co.com.bancolombia.usecase.createmessage;

import co.com.bancolombia.commons.constants.Constants;
import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.template.dto.MessageRequest;
import co.com.bancolombia.model.template.dto.MessageResponse;
import co.com.bancolombia.model.template.dto.TemplateRequest;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
public class CreateMessageUseCase {

    private final TemplateRepository templateRepository;

    public Mono<MessageResponse> createMessage(Map<String, String> header) {
        return templateRepository.getTemplate(header.get(Constants.ID_TEMPLATE))
                .map(templateRequest ->
                        MessageResponse.builder()
                                .idTemplate(templateRequest.getIdTemplate())
                                .messageSubject(templateRequest.getMessageSubject())
                                .messageBody("")
                                .plainText("")
                                .build()
                )
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
    }
}
