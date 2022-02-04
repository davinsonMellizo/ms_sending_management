package co.com.bancolombia.events;

import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.events.handlers.Handler;
import co.com.bancolombia.events.model.ResourceQuery;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.s3bucket.S3AsynOperations;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;

import static co.com.bancolombia.events.commons.EventNameConfig.SEND_ALERT;
import static org.reactivecommons.async.api.HandlerRegistry.register;

@Configuration
@RequiredArgsConstructor
public class HandlerRegistryConfiguration {

    @Value("${aws.s3.request-config-mq-key}")
    private String configMQKey;
    @Value("${aws.s3.bucket}")
    private String bucketName;


    @Bean
    public HandlerRegistry handlerRegistry(Handler handler) {
        return register()
                .handleCommand(SEND_ALERT, command ->  handler.handleSendAlert(command, configMQKey, bucketName), String.class);

    }

}