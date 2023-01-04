package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class Util {

    public Mono<Alert> replaceParameter(Alert alert, Message message) {

        return Mono.just(alert);
    }

    private Alert replaceString(Alert alert, Message message){
        message.getParameters().forEach(parameter -> {
            try {
                String text = alert.getMessage().replace("${" + parameter.getName() + "}", parameter.getValue());
                text = text.equals(alert.getMessage())?"":"";
                alert.setMessage(text);
            }catch (Exception e){
                alert.setMessage("");
            }

        });
        return alert;
    }

}
