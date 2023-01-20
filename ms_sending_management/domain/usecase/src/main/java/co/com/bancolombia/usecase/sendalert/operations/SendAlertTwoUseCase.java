package co.com.bancolombia.usecase.sendalert.operations;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.model.alerttransaction.gateways.AlertTransactionGateway;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.usecase.sendalert.RouterProviderMailUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderPushUseCase;
import co.com.bancolombia.usecase.sendalert.RouterProviderSMSUseCase;
import co.com.bancolombia.usecase.sendalert.commons.Util;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.constants.Constants.SI;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.ALERT_TRANSACTION_NOT_FOUND;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_CONTACT;
import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMailOrMobile;

@RequiredArgsConstructor
public class SendAlertTwoUseCase {
    private final AlertGateway alertGateway;
    private final AlertTransactionGateway alertTransactionGateway;
    private final RouterProviderMailUseCase routerProviderMailUseCase;
    private final RouterProviderPushUseCase routerProviderPushUseCase;
    private final RouterProviderSMSUseCase routerProviderSMSUseCase;

    private Mono<Void> sendAlert(Alert pAlert, Message message) {
        return Mono.just(pAlert)
                .filter(alert -> alert.getPush().equalsIgnoreCase(SI))
                .flatMap(alert -> routerProviderPushUseCase.sendPush(message, alert))
                .switchIfEmpty(routerProviderSMSUseCase.validateMobile(message, pAlert))
                .concatWith(routerProviderMailUseCase.routeAlertMail(message, pAlert))
                .thenEmpty(Mono.empty());
    }

    public Mono<Void> validateWithCodeTrx(Message message) {
        return Flux.just(message)
                .filter(isValidMailOrMobile)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CONTACT)))
                .flatMap(alertTransactionGateway::findAllAlertTransaction)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_TRANSACTION_NOT_FOUND)))
                .map(AlertTransaction::getIdAlert)
                .flatMap(alertGateway::findAlertById)
                .switchIfEmpty(Mono.error(new BusinessException(ALERT_NOT_FOUND)))
                .flatMap(alert -> Util.replaceParameter(alert, message))
                .flatMap(alert -> sendAlert(alert, message))
                .then(Mono.empty());
    }
}
