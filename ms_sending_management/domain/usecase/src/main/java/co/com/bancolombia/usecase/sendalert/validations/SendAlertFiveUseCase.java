package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.gateways.MessageGateway;
import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.model.service.Service;
import co.com.bancolombia.model.service.gateways.ServiceGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.*;

@RequiredArgsConstructor
public class SendAlertFiveUseCase {
    private final AlertGateway alertGateway;
    private final ProviderGateway providerGateway;
    private final RemitterGateway remitterGateway;
    private final ServiceGateway serviceGateway;
    private final MessageGateway messageGateway;

    //TODO validate id 5
    public Mono<Void> validateWithCodeAlert(Message message) {
        return Flux.just(message)
                .filter(isValidMailFormatOrMobile)
                .switchIfEmpty(Mono.error(new Throwable("Invalid contact data")))
                .map(Message::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .switchIfEmpty(Mono.error(new Throwable("Invalid Code alert")))
                .filter(alert -> alert.getIdState()==(0))
                .switchIfEmpty(Mono.error(new Throwable("Alert not active")))
                .flatMap(alert -> sendAlert(alert, message))
                .then(Mono.empty());
    }

    private Flux<Void> sendAlert(Alert alert, Message message) {
        return Flux.just(message)
                .filter(isValidMobile)
                .flatMap(message1 -> sendAlertMobile(alert, message))
                .switchIfEmpty(Flux.just(message))
                .filter(isValidMail)
                .flatMap(message1 -> sendAlertMail(alert, message))
                .thenMany(Flux.empty());
    }

    private Flux<Message> sendAlertMobile(Alert alert, Message message) {
        return null;
    }

    private Flux<Message> sendAlertMail(Alert alert, Message message) {
        Mono<Remitter> remitter = remitterGateway.findRemitterById(alert.getIdRemitter());
        Mono<Provider> providerSms = providerGateway.findProviderById(alert.getIdProviderSms());
        Mono<Provider> providerMail = providerGateway.findProviderById(alert.getIdProviderMail());
        Mono<Service> service = serviceGateway.findServiceById(alert.getIdService());
        //TODO: build template
        return Mono.zip(remitter,providerSms, providerMail, service)
                .map(data -> Mail.builder()
                        .remitter(data.getT1().getMail())
                        .affair(message.getMail())//TODO: build MAIL with data
                        .build())
                .flatMap(messageGateway::sendEmail)
                .thenMany(Flux.just(message));
    }
}
