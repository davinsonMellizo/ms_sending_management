package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.usecase.sendalert.commons.ValidateData.isValidMailAndMobile;

@RequiredArgsConstructor
public class SendAlertOneUseCase {


    //TODO validate id 1
    public Mono<Void> validateBasic(Message message) {
        return Mono.just(message)
                .filter(isValidMailAndMobile)
                .switchIfEmpty(Mono.error(new Throwable("Invalid Data contact")))
                .map(message1 -> Mail.builder().value(message1.getValue()).build())
                .then(Mono.empty());
    }
}
