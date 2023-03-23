package co.com.bancolombia.ibmmq.jms;

import co.com.bancolombia.commons.config.AwsProperties;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.ibmmq.model.ConnectionDTO;
import co.com.bancolombia.ibmmq.model.ConnectionData;
import co.com.bancolombia.ibmmq.model.QueueDto;
import co.com.bancolombia.ibmmq.supervisor.ReconnectSupervisor;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.s3bucket.S3AsyncOperations;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JmsManagement {

    private final S3AsyncOperations s3AsyncOperations;
    @Getter
    private final JmsExtConnectionFactory connectionFactory;
    private final LoggerBuilder loggerBuilder;
    private final AwsProperties awsProperties;

    @Autowired
    @Lazy
    @Qualifier("listenerMQ")
    MessageListener listener;
    @Value("${app.localPath}")
    private String localPath;
    @Getter
    private ExceptionListener exceptionListener;
    private ProducerManagement producerManagement;
    private QueueManagement queueManagement;
    @Getter
    private ConnectionData connectionData;
    @Getter
    private ConcurrentMap<String, ConnectionJms> connections;


    @PostConstruct
    public void init() {
        AwsProperties.S3 s3 = awsProperties.getS3();
        connectionData = connectionsMQ(s3AsyncOperations, s3.getBucket(), s3.getConfigListenerMqKey());
        downloadConfigFiles(connectionData.getConnections());
        connections = connectionsMap();
        producerManagement = new ProducerManagement(connections);
        queueManagement = new QueueManagement(connections, connectionData);
        exceptionListener = new ReconnectSupervisor(this, listener, loggerBuilder);
    }

    private ConnectionData connectionsMQ(S3AsyncOperations s3AsyncOperations, String bucket, String listerMQKey) {
        return JsonUtils.stringToType(s3AsyncOperations.getFileAsString(bucket, listerMQKey).block(),
                ConnectionData.class);
    }

    private ConcurrentMap<String, ConnectionJms> connectionsMap() {
        return connectionData.getConnections()
                .stream()
                .collect(Collectors.toConcurrentMap(ConnectionDTO::getName, this::createContext));
    }

    private ConnectionJms createContext(ConnectionDTO connectionDTO) {
        try {
            return new ConnectionJms(connectionFactory.connectionFactory(connectionDTO), exceptionListener);
        } catch (JMSException | JMSRuntimeException e) {
            loggerBuilder.error(e);
            throw new JMSRuntimeException(e.getMessage());
        }
    }

    public TextMessage getTextMessage(String connectionName, String message) {
        return connections.get(connectionName).getContext().createTextMessage(message);
    }

    public JMSProducer getJmsProducer(String name) {
        return producerManagement.getProducer(name);
    }

    public Destination getQueue(QueueDto dto) {
        return queueManagement.getQueue(dto);
    }

    public void resetConnection(String connectionName) {
        try {
            ConnectionDTO connDto = connectionData.getConnections()
                    .stream()
                    .filter(conn -> conn.getName().equals(connectionName))
                    .findFirst()
                    .orElseThrow(() -> new TechnicalException(TechnicalExceptionEnum.TECHNICAL_JMS_ERROR));

            producerManagement.removeProducer(connectionName);
            connections.get(connectionName).getConnection().close();
            connections.put(connectionName, createContext(connDto));
            queueManagement.initQueues(connectionName);
        } catch (JMSException e) {
            throw new TechnicalException(TechnicalExceptionEnum.TECHNICAL_JMS_ERROR);
        }
    }

    private void downloadConfigFiles(List<ConnectionDTO> connections) {
        try {
            for (ConnectionDTO connection : connections) {
                downloadFile(connection.getCcdtBucketName());
                downloadFile(connection.getJksBucketName());
            }
        } catch (IOException e) {
            loggerBuilder.error(e);
            throw new TechnicalException(e, TechnicalExceptionEnum.TECHNICAL_MQ_ERROR);
        }
    }

    private String downloadFile(String key) throws IOException {
        InputStream stream = s3AsyncOperations.getFileAsInputStream(awsProperties.getS3().getBucket(), key).block();
        String path = Optional.ofNullable(localPath).orElse("") + key;
        saveToFile(stream, path);
        return path;
    }

    public boolean saveToFile(InputStream stream, String path) throws IOException {
        var directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return Files.copy(stream, Paths.get(path), StandardCopyOption.REPLACE_EXISTING) > 0;
    }


    public void setMessageListener(QueueDto queue, MessageListener msgListener) {
        try {
            Destination destination = queueManagement.getQueue(queue);
            Connection connection = connections.get(queue.getConnection()).getConnection();
            connection.createSession()
                    .createConsumer(destination)
                    .setMessageListener(msgListener);
            connection.start();
        } catch (JMSException e) {
            throw new TechnicalException(e, TechnicalExceptionEnum.TECHNICAL_JMS_ERROR);
        }
    }
}