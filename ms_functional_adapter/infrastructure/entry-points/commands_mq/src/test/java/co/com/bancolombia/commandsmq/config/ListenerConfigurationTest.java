package co.com.bancolombia.commandsmq.config;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.ibmmq.MqConnector;
import co.com.bancolombia.ibmmq.jms.JmsManagement;
import co.com.bancolombia.ibmmq.model.ConnectionData;
import co.com.bancolombia.model.datatest.DataTest;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.usecase.functionaladapter.FunctionalAdapterUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import reactor.core.publisher.Mono;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_JMS_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListenerConfigurationTest {

    @InjectMocks
    private ListenerConfiguration listenerConfiguration;

    @InjectMocks
    private ListenerMQ listenerMQ;

    @Mock
    private JmsManagement management;
    @Mock
    private MqConnector mqConnector;
    @Mock
    private Message message;
    @Mock
    private FunctionalAdapterUseCase useCase;
    @Mock
    private LoggerBuilder loggerBuilder;
    @Mock
    private Environment env;

    private ConnectionData connData;
    private static final ObjectMapper mapper = new ObjectMapper();
    private String payload = "ALB32000*SEC 00001                                                                             " +
            "                                                                                                        " +
            "                             ALBALB                                     30        00000000              " +
            "  3200002      TS0001                    N0000                                                          " +
            " ALERTAS000102000000000704871ISESMS       ACTIVO    3233620240                                          " +
            "        MAIL      INACTIVO  bancolom@bancolombia.com.co                                 0123456789      " +
            "     DDSPCCJCL";

    @BeforeEach
    public void init() throws IOException {
        connData = mapper.readValue(DataTest.config, ConnectionData.class);
    }

    @Test
    void eventHandlers() {
        when(management.getConnectionData()).thenReturn(connData);
        assertThat(listenerConfiguration.eventHandlers(management, mqConnector)).isNull();
    }

    @Test
    void onMessageTest() throws JMSException {
        when(message.getBody(any())).thenReturn(payload);
        when(useCase.sendTransactionToRabbit(any())).thenReturn(Mono.empty());
        try {
            listenerMQ.onMessage(message);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void onMessageErrorTest() throws JMSException {
        when(message.getBody(any())).thenReturn(payload);
        when(useCase.sendTransactionToRabbit(any())).thenReturn(Mono.error(new TechnicalException(TECHNICAL_JMS_ERROR)));
        try {
            listenerMQ.onMessage(message);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void onMessageTestWithError() throws JMSException {
        when(message.getBody(any())).thenReturn(payload);
        when(useCase.sendTransactionToRabbit(any())).thenReturn(Mono.empty());
        try {
            listenerMQ.onMessage(message);
        }catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }


}