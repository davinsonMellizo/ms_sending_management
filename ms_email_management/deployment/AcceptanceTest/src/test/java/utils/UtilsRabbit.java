package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import models.Alert;
import models.Command;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static utils.ConectionRabbit.getConnectionFactoryProvider;

public class UtilsRabbit {

    public static String send(Map<String, Object> config, String alertJson) throws IOException, TimeoutException {
        ObjectMapper mapper = new ObjectMapper();
        Alert alert = mapper.readValue(alertJson, Alert.class);
        Command command = Command.builder()
                .name("send.alert.email")
                .commandId(UUID.randomUUID().toString())
                .data(alert)
                .build();
        String queueName = (String) config.get("queueName");
        Connection connection = getConnectionFactoryProvider(config).getConnectionFactory().newConnection();
        Channel channel = connection.createChannel();
        byte[] data = new ObjectMapper().writeValueAsBytes(command);
        channel.basicPublish("directMessages", queueName, new AMQP.BasicProperties(), data);

        channel.close();
        connection.close();
        return alertJson;
    }


}
