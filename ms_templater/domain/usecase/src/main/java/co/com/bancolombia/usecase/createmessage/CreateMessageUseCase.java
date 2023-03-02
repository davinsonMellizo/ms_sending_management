package co.com.bancolombia.usecase.createmessage;

import co.com.bancolombia.commons.constants.Constants;
import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.template.dto.MessageRequest;
import co.com.bancolombia.model.template.dto.MessageResponse;
import co.com.bancolombia.model.template.dto.Template;
import co.com.bancolombia.model.template.gateways.TemplateRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;

import static co.com.bancolombia.commons.constants.Constants.KEY_CLOSE;
import static co.com.bancolombia.commons.constants.Constants.KEY_OPEN;

@RequiredArgsConstructor
public class CreateMessageUseCase {

    private final TemplateRepository templateRepository;

    public Mono<MessageResponse> createMessage(MessageRequest messageRequest) {
        return validateTemplate(messageRequest.getIdTemplate())
                .filter(template -> template.getStatus().equals(Constants.ENABLED))
                .flatMap(template -> replaceData(messageRequest.getMessageValues(), template))
                .map(template -> MessageResponse.builder()
                        .idTemplate(template.getIdTemplate())
                        .messageSubject(template.getMessageSubject())
                        .messageBody(template.getMessageBody())
                        .plainText(template.getPlainText())
                        .build())
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_DISABLED)));
    }

    private Mono<Template> replaceData(Map<String, String> data, Template template) {
        data.forEach((s, s2) -> {
            template.setMessageSubject(template.getMessageSubject().replace(KEY_OPEN.concat(s).concat(KEY_CLOSE), s2));
            template.setMessageBody(template.getMessageBody().replace(KEY_OPEN.concat(s).concat(KEY_CLOSE), s2));
            template.setPlainText(template.getPlainText().replace(KEY_OPEN.concat(s).concat(KEY_CLOSE), s2));
        });
        return Mono.just(template);
    }

    public Mono<Template> validateTemplate(String idTemplate) {
        return templateRepository.getTemplate(idTemplate)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.TEMPLATE_NOT_FOUND)));
    }
}
