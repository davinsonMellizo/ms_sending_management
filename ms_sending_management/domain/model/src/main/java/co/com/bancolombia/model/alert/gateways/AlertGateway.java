package co.com.bancolombia.model.alert.gateways;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlertGateway {
    Mono<Alert> findAlertById(String id);
    Flux<Alert> findAlertByTrx(Message message);
}
