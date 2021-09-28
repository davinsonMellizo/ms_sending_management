package co.com.bancolombia.usecase.sendalert.validations;

import co.com.bancolombia.model.message.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SendAlertThreeUseCase {

    //TODO validate id 3 y 4
    public Mono<Void> validateOthersChannels(Message message) {
        return Mono.empty();
    }
    private  Mono<Void> buildMessageFrame(){
        return Mono.empty();
    }

}
