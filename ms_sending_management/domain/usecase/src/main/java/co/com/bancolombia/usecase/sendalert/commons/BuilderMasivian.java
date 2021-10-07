package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alerttemplate.AlertTemplate;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.Parameter;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@UtilityClass
public class BuilderMasivian {
    public Flux<Parameter> buildParameter(AlertTemplate alertTemplate, String messageAlert){
        return Flux.just(messageAlert.substring(alertTemplate.getInitialPosition(), alertTemplate.getFinalPosition()))
                .map(value -> Parameter.builder().Value(value).Name(alertTemplate.getField()).build());
    }
}
