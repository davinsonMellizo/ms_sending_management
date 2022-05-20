package co.com.bancolombia.ibmmq.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JMSExtExceptionTest {

    private static final String queueName = "DEV.QUEUE.1";
    private static final String message = "message";

    @Test
    void getQueueTest() {
        assertThat(new JMSExtException(message, queueName)).isNotNull();
    }

}
