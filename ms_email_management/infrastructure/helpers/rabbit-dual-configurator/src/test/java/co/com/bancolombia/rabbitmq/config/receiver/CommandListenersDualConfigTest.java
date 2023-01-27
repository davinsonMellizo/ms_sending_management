package co.com.bancolombia.rabbitmq.config.receiver;

import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.rabbitmq.config.dual.receiver.CommandListenersDualConfig;
import com.rabbitmq.client.ConnectionFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.async.commons.DiscardNotifier;
import org.reactivecommons.async.commons.converters.MessageConverter;
import org.reactivecommons.async.commons.ext.CustomReporter;
import org.reactivecommons.async.rabbit.HandlerResolver;
import org.reactivecommons.async.rabbit.config.props.AsyncProps;
import org.reactivecommons.async.rabbit.listeners.ApplicationCommandListener;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CommandListenersDualConfigTest {
    @Mock
    private LoggerBuilder logger;
    private final AsyncProps props = new AsyncProps();
    private CommandListenersDualConfig config;

    private final ConnectionFactory factory = mock(ConnectionFactory.class);
    private final HandlerResolver handlerResolver = mock(HandlerResolver.class);
    private final MessageConverter messageConverter = mock(MessageConverter.class);
    private final DiscardNotifier discardNotifier = mock(DiscardNotifier.class);
    private final CustomReporter customReporter = mock(CustomReporter.class);

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        config = new CommandListenersDualConfig(props, logger);
        final Field appName = CommandListenersDualConfig.class.getDeclaredField("appName");
        appName.setAccessible(true);
        appName.set(config, "queue");
    }

    @Test
    void applicationCommandListener() {
        final ApplicationCommandListener commandListener = config.applicationCommandListener(
                messageConverter, factory,
                discardNotifier, handlerResolver,
                customReporter
        );
        Assertions.assertThat(commandListener).isNotNull();
    }
}
