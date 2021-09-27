package co.com.bancolombia.config.model.log.gateways;

import co.com.bancolombia.config.model.alert.Alert;
import reactor.core.publisher.Mono;

public interface LogGateway {
    Mono<Boolean> createSendingLog(String data);
    Mono<String> listSendingLogs();
    Mono<Alert> getDataSendingLog();
}
