package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Parameter;
import co.com.bancolombia.model.message.TemplateEmail;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;


@UtilityClass
public class Util {
    private static String target = "${message}";
    public Mono<TemplateEmail> replaceParameter(Alert alert, TemplateEmail templateEmail) {
        return Mono.just(templateEmail)
                .map(templateEmail1 -> replaceParameterTemplate(alert, templateEmail))
                .map(templateEmail1 -> templateEmail1.toBuilder()
                        .bodyHtml(templateEmail1.getBodyHtml().replace(target, templateEmail.getTextPlain()))
                        .build());
    }

    private TemplateEmail replaceParameterTemplate(Alert alert, TemplateEmail templateEmail) {
        alert.getTemplate().getParameters().forEach(parameter -> {
            String message = templateEmail.getTextPlain().replace("${" + parameter.getName() + "}", parameter.getValue());
            templateEmail.setTextPlain(message);
        });

        return templateEmail;
    }

}
