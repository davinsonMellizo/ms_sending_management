package co.com.bancolombia.rabbitmq.config.dual.receiver;

import co.com.bancolombia.model.log.LoggerBuilder;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.commons.DiscardNotifier;
import org.reactivecommons.async.commons.converters.MessageConverter;
import org.reactivecommons.async.commons.ext.CustomReporter;
import org.reactivecommons.async.rabbit.HandlerResolver;
import org.reactivecommons.async.rabbit.communications.ReactiveMessageListener;
import org.reactivecommons.async.rabbit.communications.TopologyCreator;
import org.reactivecommons.async.rabbit.config.ConnectionFactoryProvider;
import org.reactivecommons.async.rabbit.config.props.AsyncProps;
import org.reactivecommons.async.rabbit.listeners.ApplicationCommandListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

import static co.com.bancolombia.rabbitmq.config.dual.commons.Utils.createConnectionMono;

@Configuration
@RequiredArgsConstructor
public class CommandListenersDualConfig {
    private static final String LISTENER_TYPE = "listener";
    private final AsyncProps asyncProps;
    private final LoggerBuilder logger;

    @Value("${spring.application.name}")
    private String appName;


    @Bean("ApplicationCommandListenerDual")
    public ApplicationCommandListener applicationCommandListener(MessageConverter converter,
                                                                 ConnectionFactory factory,
                                                                 DiscardNotifier discardNotifier,
                                                                 HandlerResolver resolver,
                                                                 CustomReporter errorReporter) {

        ReactiveMessageListener listener = messageListenerDual(factory);
        ApplicationCommandListener commandListener = new ApplicationCommandListener(listener, appName, resolver,
                asyncProps.getDirect().getExchange(), converter, asyncProps.getWithDLQRetry(), asyncProps.getMaxRetries(),
                asyncProps.getRetryDelay(), asyncProps.getDirect().getMaxLengthBytes(), discardNotifier, errorReporter);

        commandListener.startListener();
        return commandListener;
    }

    private ReactiveMessageListener messageListenerDual(ConnectionFactory factory) {
        ConnectionFactoryProvider provider = () -> factory;
        final Mono<Connection> connection =
                createConnectionMono(provider.getConnectionFactory(), appName, LISTENER_TYPE, logger);
        final Receiver receiver = RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(connection));
        final Sender sender = RabbitFlux.createSender(new SenderOptions().connectionMono(connection));

        return new ReactiveMessageListener(receiver,
                new TopologyCreator(sender),
                asyncProps.getFlux().getMaxConcurrency(),
                asyncProps.getPrefetchCount());
    }

}
