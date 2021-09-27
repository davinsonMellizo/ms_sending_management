package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.config.model.alert.Alert;
import co.com.bancolombia.config.model.alert.gateways.AlertGateway;
import co.com.bancolombia.config.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.config.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.config.model.message.Mail;
import co.com.bancolombia.config.model.message.Message;
import co.com.bancolombia.config.model.message.gateways.MessageGateway;
import co.com.bancolombia.config.model.provider.Provider;
import co.com.bancolombia.config.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.config.model.remitter.Remitter;
import co.com.bancolombia.config.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.config.model.service.Service;
import co.com.bancolombia.config.model.service.gateways.ServiceGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.*;

@RequiredArgsConstructor
public class SendAlertTwoUseCase {
    private final AlertGateway alertGateway;
    private final AlertTransactionGateway alertTransactionGateway;
    private final ProviderGateway providerGateway;
    private final RemitterGateway remitterGateway;
    private final ServiceGateway serviceGateway;
    private final MessageGateway messageGateway;

    //TODO validate id 2
    public Mono<Void> validateWithCodeTrx(Message message) {
        return Flux.just(message)
                .filter(isValidMailAndMobile)
                .switchIfEmpty(Mono.error(new Throwable("Invalid Data contact")))
                .flatMap(alertTransactionGateway::findAllAlertTransaction)
                .map(AlertTransaction::getIdAlert)
                .flatMap(alertGateway::findAlertById)
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
