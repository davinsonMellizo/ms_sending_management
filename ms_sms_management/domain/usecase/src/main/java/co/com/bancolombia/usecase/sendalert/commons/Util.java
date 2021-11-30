package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.TemplateSms;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class Util {
    public Mono<TemplateSms> replaceParameter(Alert alert, TemplateSms templateSms) {
        alert.getTemplate().getParameters().forEach(parameter -> {
            String message = templateSms.getBodyText().replace("${" + parameter.getName() + "}", parameter.getValue());
            templateSms.setBodyText(message);
        });

        return Mono.just(templateSms);
    }

}
