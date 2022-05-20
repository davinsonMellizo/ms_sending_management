package co.com.bancolombia.ibmmq.model;

import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.model.datatest.DataTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConnectionDataTest {

    private static final ConnectionData connectionData = JsonUtils.stringToType(DataTest.config, ConnectionData.class);
    private static final String queueName = "DEV.QUEUE.1";
    private static final String keyConnect = "ALM9610";

    @Test
    void getQueueTest() {
        assertThat(connectionData.getQueue(queueName)).isNotNull();
    }

    @Test
    void getListenerTest() {
        assertThat(connectionData.getListener(keyConnect)).isNotNull();
    }

    @Test
    void getQueueFromTransactionTest() {
        assertThat(connectionData.getQueueFromTransaction(keyConnect)).isNotNull();
    }

    @Test
    void getQueueResponseFromTransactionTest() {
        assertThat(connectionData.getQueueResponseFromTransaction(keyConnect)).isNotNull();
    }

    @Test
    void getTemplateTest() {
        assertThat(connectionData.getTemplate(keyConnect)).isNotNull();
    }

    @Test
    void getConnectionsTest() {
        assertThat(connectionData.getConnections()).isNotNull();
    }

}
