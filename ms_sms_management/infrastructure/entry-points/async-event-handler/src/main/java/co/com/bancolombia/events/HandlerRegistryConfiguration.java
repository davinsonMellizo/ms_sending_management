package co.com.bancolombia.events;

import co.com.bancolombia.events.handlers.CommandsHandler;
import co.com.bancolombia.model.message.Alert;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Retryable;

import java.util.Map;

import static co.com.bancolombia.events.commons.EventNameConfig.SEND_ALERT;
import static org.reactivecommons.async.api.HandlerRegistry.register;

@Configuration
public class HandlerRegistryConfiguration {

    @Bean
    public HandlerRegistry handlerRegistry(CommandsHandler commands) {
        return register()
                .handleCommand(SEND_ALERT, commands::handleSendAlert, Alert.class);
    }


    }


