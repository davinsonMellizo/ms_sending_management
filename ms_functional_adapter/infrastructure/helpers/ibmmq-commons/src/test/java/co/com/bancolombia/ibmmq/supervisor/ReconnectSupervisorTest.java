package co.com.bancolombia.ibmmq.supervisor;

import co.com.bancolombia.ibmmq.jms.JmsManagement;
import co.com.bancolombia.model.log.LoggerBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jms.MessageListener;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReconnectSupervisorTest {
    @Mock
    JmsManagement wrapper;
    @Mock
    MessageListener listener;
    @Mock
    LoggerBuilder loggerBuilder;

    @Test
    void shouldStartWorkerOnError() {
        ReconnectSupervisor supervisor = new ReconnectSupervisor(wrapper, listener, loggerBuilder);
        assertThat(supervisor.isRunning()).isFalse();
    }
}