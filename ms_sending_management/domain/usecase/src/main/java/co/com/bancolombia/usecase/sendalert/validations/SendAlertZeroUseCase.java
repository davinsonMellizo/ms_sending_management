package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
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
public class SendAlertZeroUseCase {
    private final AlertTransactionGateway alertTransactionGateway;
    private final AlertClientGateway alertClientGateway;
    private final ProviderGateway providerGateway;
    private final RemitterGateway remitterGateway;
    private final ConsumerGateway consumerGateway;
    private final ServiceGateway serviceGateway;
    private final MessageGateway messageGateway;
    private final ContactGateway contactGateway;
    private final ClientGateway clientGateway;
    private final AlertGateway alertGateway;

    private Mono<Alert> validateAmount(Alert alert, Message message){
        return Mono.just(AlertClient.builder().build())
                .flatMap(alertClientGateway::findAlertClient)
                .switchIfEmpty(Mono.error(new Throwable("Business client alert not found")))
                .filter(alertClient -> alertClient.getAccumulatedAmount()+message.getAmount()<=alertClient.getAmountEnable())
                .map(alertClient -> alert)
                .switchIfEmpty(Mono.error(new Throwable("Business amount exceeded")));
    }
    private Flux<Alert> validateObligation(Alert pAlert, Message message){
        return Flux.just(pAlert)
                .filter(alert -> !alert.getObligatory())
                .filter(alert -> alert.getNature().equals("MO"))
                .flatMap(alert -> validateAmount(alert, message))
                .switchIfEmpty(Mono.just(pAlert));
    }

    private Mono<Void> validateAlerts(Message message){
        return alertTransactionGateway.findAllAlertTransaction(message)
                .map(AlertTransaction::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .flatMap(alert -> validateObligation(alert, message))
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
       /* Mono<Remitter> remitter = remitterGateway.findRemitterById(alert.getIdRemitter());
        Mono<Provider> providerSms = providerGateway.findProviderById(alert.getIdProviderSms());
        Mono<Provider> providerMail = providerGateway.findProviderById(alert.getIdProviderMail());

        //TODO: build template
        return Mono.zip(remitter,providerSms, providerMail)
                .map(data -> Mail.builder()
                        .remitter(data.getT1().getMail())
                        .affair(message.getMail())//TODO: build MAIL with data
                        .build())
                .flatMap(messageGateway::sendEmail)
                .thenMany(Flux.just(message));*/
        return Flux.empty();
    }

    private Mono<Message> findDataContact(Message message){
        return contactGateway.findAllContactsByClient(message)
                .collectMap(Contact::getContactMedium)
                .map(contacts -> Message.builder()
                        .mobile(contacts.get("SMS").getValue())
                        .mail(contacts.get("MAIL").getValue()).build())
                .switchIfEmpty(Mono.just(message));
    }

    private Mono<Message> validateDataContact(Message message, Consumer consumer){
        return Mono.just(message)
                .filter(isValidMailAndMobile)
                .switchIfEmpty(findDataContact(message))
                .filter(isValidMailAndMobile)
                .switchIfEmpty(findDataContact(Message.builder().consumer(consumer.getSegment()).build()))
                .filter(isValidMailAndMobile)
                .switchIfEmpty(Mono.error(new Throwable("Invalid data contact")));

    }

    public Mono<Void> validateWithDataClient(Message message) {
        return Mono.just(message)
                .flatMap(clientGateway::findClientByIdentification)
                .switchIfEmpty(Mono.error(new Throwable("Client not found")))
                .filter(client -> client.getIdState()==0)
                .switchIfEmpty(Mono.error(new Throwable("Client not active")))
                .flatMap(client -> consumerGateway.findConsumerById(message.getConsumer()))
                .switchIfEmpty(Mono.error(new Throwable("Invalid Consumer")))
                .flatMap(consumer -> validateDataContact(message, consumer))
                .flatMap(this::validateAlerts)
                .then(Mono.empty());
    }
}
