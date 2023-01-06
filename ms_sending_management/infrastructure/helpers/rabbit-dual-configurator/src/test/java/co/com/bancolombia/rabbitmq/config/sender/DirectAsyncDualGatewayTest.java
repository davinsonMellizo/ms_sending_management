package co.com.bancolombia.rabbitmq.config.sender;

import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.rabbitmq.config.dual.sender.DirectAsyncDualGateway;
import co.com.bancolombia.rabbitmq.config.dual.sender.RabbitDirectAsyncGateway;
import co.com.bancolombia.rabbitmq.config.dual.sender.RabbitMQDualConfigHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.From;
import org.reactivecommons.async.impl.communications.Message;
import org.reactivecommons.async.impl.communications.ReactiveMessageSender;
import org.reactivecommons.async.impl.config.BrokerConfig;
import org.reactivecommons.async.impl.converters.MessageConverter;
import org.reactivecommons.async.impl.reply.ReactiveReplyRouter;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectAsyncDualGatewayTest {
    @MockBean
    private LoggerBuilder logger;
    private final BrokerConfig brokerConfig = mock(BrokerConfig.class);
    private final ReactiveReplyRouter router = mock(ReactiveReplyRouter.class);
    private final ReactiveMessageSender sender = mock(ReactiveMessageSender.class);
    private final MessageConverter messageConverter = mock(MessageConverter.class);

    private final DirectAsyncDualGateway rabbitDirectAsyncGateway = new RabbitDirectAsyncGateway(
            brokerConfig,
            router,
            sender,
            "exchange",
            messageConverter
    );


    @Test
    void sendCommandTest() {
        when(sender.sendWithConfirm(any(), anyString(), anyString(), any(), anyBoolean())).thenReturn(Mono.empty());
        Command command = new Command<>("name", "id", "data");
        StepVerifier.create(rabbitDirectAsyncGateway.sendCommand(command,"")).verifyComplete();
    }

    @Test
    void replyTest() {
        when(sender.sendNoConfirm(any(), anyString(), anyString(), any(), anyBoolean())).thenReturn(Mono.empty());
        From from = new From();
        from.setReplyID("");
        from.setCorrelationID("");
        StepVerifier.create(rabbitDirectAsyncGateway.reply("",from)).verifyComplete();
    }

    @Test
    void requestReplyTest() {
        when(sender.sendNoConfirm(any(), anyString(), anyString(), any(), anyBoolean())).thenReturn(Mono.empty());
        when(router.register(anyString())).thenReturn(Mono.empty());
        when(messageConverter.readValue(any(), any())).thenReturn("mensaje");
        AsyncQuery asyncQuery = new AsyncQuery("name", "id");
        StepVerifier.create(rabbitDirectAsyncGateway.requestReply(asyncQuery,"", String.class)).verifyComplete();
    }
}
