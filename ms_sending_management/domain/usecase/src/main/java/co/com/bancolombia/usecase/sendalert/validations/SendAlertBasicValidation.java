package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.message.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMailAndMobile;

@RequiredArgsConstructor
public class SendAlertBasicValidation {
    private final AlertGateway alertGateway;

    public Mono<Void> validate(Message message) {
        return Flux.just(message)
                .doOnNext(message1 -> System.out.println("Validate consumer"))
                .filter(isValidMailAndMobile)
                .flatMap(this::validateData)
                .then(Mono.empty());
    }

    private Flux<Alert> validateData(Message message) {
        return Flux.just(message)
                .filter(message1 -> message1.getValue().isEmpty())
                .switchIfEmpty(Mono.error(new Throwable("Alert with message case 1")))
                .map(Message::getIdAlert)
                .filter(String::isEmpty)
                .map(s -> message)
                .flatMap(this::getAlertsWithTrx)
                .switchIfEmpty(this.getAlert(message));
    }

    private Flux<Alert> getAlertsWithTrx(Message message) {
        //TODO case 2, 0, 3y4
        return Flux.just(message)
                .map(Message::getDocumentNumber)
                .map(aLong ->  Long.toString(aLong))
                .filter(s -> !s.isEmpty())
                .flatMap(this::getAlertWithDocument)
                .switchIfEmpty(Mono.empty());//TODO get alert by Trx, build and send alert case 2
    }

    private Flux<Alert> getAlertWithDocument(String s) {
        return Flux.empty();
    }

    private Flux<Alert> getAlert(Message message) {
        //TODO case 5
        return alertGateway.findAlertById(message.getIdAlert())
                .switchIfEmpty(Mono.error(new Throwable("Business alert no fount")))
                .doOnNext(alert -> System.out.println("build and send alert"))
                .flux();
    }
}
