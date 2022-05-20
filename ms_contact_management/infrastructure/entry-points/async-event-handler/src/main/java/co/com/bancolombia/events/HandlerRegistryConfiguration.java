package co.com.bancolombia.events;

import co.com.bancolombia.events.handlers.CommandsHandler;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static co.com.bancolombia.events.config.EventNameConfig.SEND_CREATE_ISERIES;
import static co.com.bancolombia.events.config.EventNameConfig.SEND_UPDATE_ISERIES;
import static org.reactivecommons.async.api.HandlerRegistry.register;

@Configuration
public class HandlerRegistryConfiguration {

    @Bean
    public HandlerRegistry handlerRegistry(CommandsHandler commands) {
        return register()
                .handleCommand(SEND_CREATE_ISERIES, commands::saveClient, String.class)
                .handleCommand(SEND_UPDATE_ISERIES, commands::updateClient, String.class);
    }
}