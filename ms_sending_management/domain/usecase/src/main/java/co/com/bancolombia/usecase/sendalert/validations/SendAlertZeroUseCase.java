package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.alertclient.gateways.AlertClientGateway;
import co.com.bancolombia.model.alerttemplate.gateways.AlertTemplateGateway;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.client.gateways.ClientGateway;
import co.com.bancolombia.model.consumer.Consumer;
import co.com.bancolombia.model.consumer.gateways.ConsumerGateway;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.gateways.ContactGateway;
import co.com.bancolombia.model.message.*;
import co.com.bancolombia.model.provider.gateways.ProviderGateway;
import co.com.bancolombia.model.providerservice.ProviderService;
import co.com.bancolombia.model.providerservice.gateways.ProviderServiceGateway;
import co.com.bancolombia.model.remitter.Remitter;
import co.com.bancolombia.model.remitter.gateways.RemitterGateway;
import co.com.bancolombia.usecase.Response;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
import co.com.bancolombia.usecase.sendalert.commons.BuilderMasivian;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static co.com.bancolombia.commons.constants.State.ACTIVE;
import static co.com.bancolombia.commons.constants.TypeLogSend.SEND_220;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;

@RequiredArgsConstructor
public class SendAlertZeroUseCase {
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;
    private final AlertTransactionGateway alertTransactionGateway;
    private final AlertTemplateGateway alertTemplateGateway;
    private final AlertClientGateway alertClientGateway;
    private final RemitterGateway remitterGateway;
    private final ConsumerGateway consumerGateway;
    private final ContactGateway contactGateway;
    private final ClientGateway clientGateway;
    private final AlertGateway alertGateway;
    private final LogUseCase logUseCase;

    private Mono<Alert> validateAmount(Alert alert, Message message){
        return Mono.just(AlertClient.builder().idAlert(alert.getId()).documentType(message.getDocumentType())
                .documentNumber(message.getDocumentNumber()).build())
                .flatMap(alertClientGateway::findAlertClient)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_CLIENT_NOT_FOUND)))
                .filter(alertClient -> alertClient.getAccumulatedAmount()+message.getAmount()>=alertClient.getAmountEnable() ||
                        alertClient.getAccumulatedOperations()+1>=alertClient.getNumberOperations())
                .map(alertClient -> alert)
                .switchIfEmpty(Mono.error(new BusinessException(AMOUNT_NOT_EXCEEDED)));
    }
    private Flux<Alert> validateObligation(Alert pAlert, Message message){
        return Flux.just(pAlert)
                .filter(alert -> !alert.getObligatory())
                .filter(alert -> alert.getNature().equals("MO"))
                .flatMap(alert -> validateAmount(alert, message))
                .switchIfEmpty(Mono.just(pAlert))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message,SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())));
    }

    private Mono<Void> validateAlerts(Message message){
        return alertTransactionGateway.findAllAlertTransaction(message)
                .map(AlertTransaction::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .switchIfEmpty(logUseCase.sendLogError(message,SEND_220, new Response(1, ALERT_NOT_FOUND)))
                .flatMap(alert -> Util.replaceParameter(alert, message))
                .flatMap(alert -> validateObligation(alert, message))
                .flatMap(alert -> sendAlerts(alert, message))
                .then(Mono.empty());
    }

    private Flux<Void> sendAlerts(Alert alert, Message message) {
        return Flux.just(message)
                .filter(message1 -> !message1.getPush() || alert.getPush().equalsIgnoreCase("No"))
                .flatMap(message1 -> routerProviderSMSUseCase.routingAlertsSMS(message1, alert))
                .switchIfEmpty(routerProviderPushUseCase.sendPush(message,alert))
                .concatWith(message1 -> sendAlertMail(alert, message))
                .thenMany(Flux.empty());
    }

    private Mono<Mail> buildMail(Alert alert, Message message, Mail mail){
        ArrayList<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(message.getMail()));
        return alertTemplateGateway.findTemplateById(alert.getIdTemplate())
                .flatMap(alertTemplate -> BuilderMasivian.buildParameter(alertTemplate, alert.getMessage()))
                .map(parameter -> mail.getParameters().add(parameter))
                .last()
                .flatMap(aBoolean ->  remitterGateway.findRemitterById(alert.getIdRemitter()))
                .map(remitter -> mail.toBuilder()
                        .Subject(alert.getSubjectMail())
                        .Recipients(recipients)
                        .From(remitter.getMail())
                        .Attachments(Util.validateAttachments(message.getAttachments()))
                        .Template(new Template())// TODO find template
                        .build());
    }
    private Flux<Response> sendAlertMail(Alert alert, Message message) {
        buildMail(alert, message, Mail.builder().Subject(alert.getSubjectMail()).build())
                .thenReturn(message);


        //TODO::Implement routing proveedor
        //TODO::Implement send sms
        Mono<Remitter> remitter = remitterGateway.findRemitterById(alert.getIdRemitter());
        /*Mono<Provider> providerSms = providerGateway.findProviderById(alert.getIdProviderSms());
        Mono<Provider> providerMail = providerGateway.findProviderById(alert.getIdProviderMail());*/


        return Flux.empty();
    }


    //TODO:VALIDATE DATA CONTACT
    private Mono<Message> validateDataContact(Message message, Consumer consumer){
        return contactGateway.findAllContactsByClient(message.toBuilder().consumer(consumer.getSegment()).build())
                .collectMap(Contact::getContactMedium)
                .map(contacts -> message.toBuilder()
                        .phone(contacts.get("SMS")!=null?contacts.get("SMS").getValue():"")
                        .push(contacts.get("PUSH")!=null? contacts.get("PUSH").getIdState()==ACTIVE? true: false :false)
                        .mail(contacts.get("MAIL")!=null?contacts.get("MAIL").getValue():"").build())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACTS)))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message,SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())));
    }

    public Mono<Void> validateWithDataClient(Message message) {
        return Mono.just(message)
                .flatMap(message1 -> clientGateway.findClientByIdentification(message.getDocumentNumber(), message.getDocumentType()))
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_NOT_FOUND)))
                .filter(client -> client.getIdState()==0)
                .switchIfEmpty(Mono.error(new BusinessException(CLIENT_INACTIVE)))
                .flatMap(client -> consumerGateway.findConsumerById(message.getConsumer()))
                .switchIfEmpty(Mono.error(new BusinessException(CONSUMER_NOT_FOUND)))
                .onErrorResume(BusinessException.class, e -> logUseCase.sendLogError(message,SEND_220,
                        new Response(1, e.getBusinessErrorMessage().getMessage())))
                .flatMap(consumer -> validateDataContact(message, consumer))
                .flatMap(this::validateAlerts)
                .onErrorResume(TechnicalException.class, e -> logUseCase.sendLogError(message,SEND_220,
                        new Response(1, e.getMessage())));
    }
}
