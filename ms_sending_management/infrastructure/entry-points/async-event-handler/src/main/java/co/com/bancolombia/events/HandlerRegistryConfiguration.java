package co.com.bancolombia.events;

import co.com.bancolombia.events.handlers.CommandsHandler;
import co.com.bancolombia.events.handlers.EventsHandler;
import co.com.bancolombia.model.message.Message;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static co.com.bancolombia.events.commons.EventNameConfig.SEND_ALERT;
import static co.com.bancolombia.events.commons.EventNameConfig.SEND_ALERT_RETRY;
import static org.reactivecommons.async.api.HandlerRegistry.register;

@Configuration
public class HandlerRegistryConfiguration {

    @Bean
    public HandlerRegistry handlerRegistry(CommandsHandler commands, EventsHandler events) {
        return register()
                .handleCommand(SEND_ALERT, commands::handleSendAlert, Message.class)
                .listenEvent(SEND_ALERT_RETRY, events::handleSendAlert, Message.class);
    }
}