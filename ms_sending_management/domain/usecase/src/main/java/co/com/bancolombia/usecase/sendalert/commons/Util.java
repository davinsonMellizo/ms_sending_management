package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Attachment;
import co.com.bancolombia.model.message.Message;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

@UtilityClass
public class Util {
    public Flux<Alert> replaceParameter(Alert alert, Message message){
        return Flux.fromIterable(message.getParameters())
                .map(parameter -> alert.getMessage().replace("<"+parameter.getName()+">",parameter.getValue()))
                .map(s -> alert.message(s))
                .thenMany(Flux.just(alert));
    }

    public ArrayList<Attachment> validateAttachments(ArrayList<Attachment> attachments){
        return attachments == null? null : attachments.isEmpty()? null : attachments;
    }
}
