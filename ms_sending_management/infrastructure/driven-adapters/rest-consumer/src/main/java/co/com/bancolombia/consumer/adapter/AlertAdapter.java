package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.gateways.MessageGateway;
import co.com.bancolombia.consumer.ObjectRequest;
import co.com.bancolombia.consumer.ObjectResponse;
import co.com.bancolombia.consumer.RestConsumer;
import co.com.bancolombia.consumer.config.ClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AlertAdapter implements MessageGateway {
    private final ClientProperties properties;
    private final RestConsumer<ObjectRequest, ObjectResponse> restConsumer;

    @Override
    public Mono<Void> sendEmail(Mail mail) {
        return restConsumer.get(properties.getResources().getSendAlertMail(), ObjectResponse.class)
                .then(Mono.empty());
    }
}
