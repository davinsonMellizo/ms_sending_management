package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alerttemplate.AlertTemplate;
import co.com.bancolombia.model.message.Attachment;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Parameter;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_PARAMETERS;

@UtilityClass
public class Util {
    public Flux<Alert> replaceParameter(Alert alert, Message message) {
        return Flux.fromIterable(message.getParameters())
                .flatMap(parameter -> containParameter(parameter, alert))
                .map(parameter -> alert.getMessage().replace("<" + parameter.getName() + ">", parameter.getValue()))
                .map(s -> alert.message(s)).last()
                .filter(alert1 -> !alert.getMessage().contains("<"))
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_PARAMETERS)))
                .thenMany(Flux.just(alert));
    }

    private Flux<Parameter> containParameter(Parameter pParameter, Alert alert) {
        return Flux.just(pParameter)
                .filter(parameter -> alert.getMessage().contains("<" + parameter.getName() + ">"))
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_PARAMETERS)));
    }

    public ArrayList<Attachment> validateAttachments(ArrayList<Attachment> attachments) {
        return attachments == null ? null : attachments.isEmpty() ? null : attachments;
    }

    public Flux<Parameter> buildParameter(AlertTemplate alertTemplate, String messageAlert) {
        return Flux.just(messageAlert.substring(alertTemplate.getInitialPosition(), alertTemplate.getFinalPosition()))
                .map(value -> Parameter.builder().Value(value).Name(alertTemplate.getField()).build());
    }
}
