package co.com.bancolombia.events;

import co.com.bancolombia.events.handlers.CommandsHandler;
import co.com.bancolombia.model.log.Log;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static co.com.bancolombia.events.config.EventNameConfig.SAVE_LOG_SEND_ALERT;
import static org.reactivecommons.async.api.HandlerRegistry.register;

@Configuration
public class HandlerRegistryConfiguration {

    @Bean
    public HandlerRegistry handlerRegistry(CommandsHandler commands) {
        return register()
                .handleCommand(SAVE_LOG_SEND_ALERT, commands::saveLog, Log.class)

                .listenEvent();
    }
}