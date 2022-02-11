package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class Util {

    public Mono<Alert> replaceParameter(Alert alert, Message message) {
        message.getParameters().forEach(parameter -> {
            String text = alert.getMessage().replace("${" + parameter.getName() + "}", parameter.getValue());
            alert.setMessage(text);
        });

        return Mono.just(alert);
    }

}
