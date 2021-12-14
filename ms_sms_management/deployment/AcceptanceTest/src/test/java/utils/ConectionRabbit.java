package utils;

import com.rabbitmq.client.ConnectionFactory;
import org.reactivecommons.async.impl.config.ConnectionFactoryProvider;

import java.util.Map;

public class ConectionRabbit {
    public static ConnectionFactoryProvider getConnectionFactoryProvider(Map<String, Object> config){

        final ConnectionFactory factory = new ConnectionFactory();

        factory.setHost((String) config.get("host"));
        factory.setPort((Integer) config.get("port"));
        factory.setUsername((String) config.get("username"));
        factory.setPassword((String) config.get("password"));

        return () -> factory;
    }


}
