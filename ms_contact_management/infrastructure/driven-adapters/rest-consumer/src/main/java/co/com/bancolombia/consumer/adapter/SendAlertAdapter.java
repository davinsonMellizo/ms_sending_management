package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.consumer.RestConsumerInternal;
import co.com.bancolombia.consumer.config.ConsumerProperties;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.alert.gateways.AlertGateway;
import co.com.bancolombia.model.error.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class SendAlertAdapter implements AlertGateway {

    private final ConsumerProperties properties;
    private final RestConsumerInternal<Alert, Response> restConsumerIs;


    @Override
    public Mono<Alert> sendAlert(Alert alert) {
        var headers = new HashMap<String, String>();
        alert.setHeaders(headers);
        return restConsumerIs.post(properties.getResources().getSendAlert(), alert, Response.class, Error.class)
                .thenReturn(alert);
    }
}