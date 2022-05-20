package co.com.bancolombia.commandsmq.config;

import co.com.bancolombia.ibmmq.MqConnector;
import co.com.bancolombia.ibmmq.jms.JmsManagement;
import co.com.bancolombia.ibmmq.model.ConnectionData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ListenerConfiguration {

    private final ListenerMQ listenerMQ;

    @Bean
    public ListenerMQ eventHandlers(final JmsManagement management, MqConnector mqConnector) {
        ConnectionData connData = management.getConnectionData();
        connData.getListener()
                .forEach(event -> mqConnector.eventConsumer(connData.getQueue(event.getQueueResponse()), listenerMQ));
        return null;
    }
}