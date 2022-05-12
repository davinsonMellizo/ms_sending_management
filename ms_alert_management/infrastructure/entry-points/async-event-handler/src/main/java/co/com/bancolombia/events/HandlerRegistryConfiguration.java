package co.com.bancolombia.events;

import co.com.bancolombia.events.handlers.CommandsHandler;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static co.com.bancolombia.events.commons.EventNameConfig.*;
import static org.reactivecommons.async.api.HandlerRegistry.register;

@Configuration
public class HandlerRegistryConfiguration {

    @Bean
    public HandlerRegistry handlerRegistry(CommandsHandler commands) {
        return register()
                .handleCommand(SEND_CREATE_PROVIDER, commands::saveProvider, String.class)
                .handleCommand(SEND_UPDATE_PROVIDER, commands::updateProvider, String.class)
                .handleCommand(SEND_CREATE_REMITTER, commands::saveRemitter, String.class)
                .handleCommand(SEND_UPDATE_REMITTER, commands::updateRemitter, String.class)
                .handleCommand(SEND_CREATE_ALERT, commands::saveAlert, String.class)
                .handleCommand(SEND_UPDATE_ALERT, commands::updateAlert, String.class)
                .handleCommand(SEND_CREATE_ALERT_TRX, commands::saveAlertTrx, String.class)
                .handleCommand(SEND_DELETE_ALERT_TRX, commands::deleteAlertTrx, String.class)
                .handleCommand(SEND_CREATE_CONSUMER, commands::saveConsumer, String.class)
                .handleCommand(SEND_UPDATE_CONSUMER, commands::updateConsumer, String.class);
    }
}