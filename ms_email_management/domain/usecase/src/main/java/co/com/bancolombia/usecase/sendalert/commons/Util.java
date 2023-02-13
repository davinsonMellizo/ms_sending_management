package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.TemplateEmail;
import co.com.bancolombia.model.message.gateways.TemplateGateway;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;


@UtilityClass
public class Util {
    public Mono<TemplateEmail> validTemplate (Alert pAlert){
        return Mono.just(pAlert)
                .filter(alert->alert.getMessage()!=null)
                .map(alert -> TemplateEmail.builder().subject(alert.getMessage().getSubject())
                        .bodyHtml(alert.getMessage().getBody()).build());
    }


}
