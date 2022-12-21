package co.com.bancolombia.events;

import co.com.bancolombia.events.handlers.CommandsHandler;
import org.reactivecommons.async.api.HandlerRegistry;
import org.reactivecommons.async.impl.communications.Message;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;





import static co.com.bancolombia.events.commons.EventNameConfig.SEND_ALERT;
import static org.reactivecommons.async.api.HandlerRegistry.register;

@Configuration
public class HandlerRegistryConfiguration {

    @Bean
    public HandlerRegistry handlerRegistry( CommandsHandler commands) {
        return register()
                .handleCommand(SEND_ALERT, commands::handleSendAlert, Message.class);
    }

    }


