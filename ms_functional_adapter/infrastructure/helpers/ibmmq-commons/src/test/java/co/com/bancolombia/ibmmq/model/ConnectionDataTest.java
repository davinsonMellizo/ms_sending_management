package co.com.bancolombia.ibmmq.model;

import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.model.datatest.DataTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConnectionDataTest {

    private static final ConnectionData connectionData = JsonUtils.stringToType(DataTest.config, ConnectionData.class);
    private static final String queueName =  "DEV.QUEUE.1";
    private static final String keyConnect = "ALM9610";

    @Test
    public void getQueueTest(){
        assertThat(connectionData.getQueue(queueName)).isNotNull();
    }

    @Test
    public void getListenerTest(){
        assertThat(connectionData.getListener(keyConnect)).isNotNull();
    }

    @Test
    public void getQueueFromTransactionTest(){
        assertThat(connectionData.getQueueFromTransaction(keyConnect)).isNotNull();
    }
    @Test
    public void getQueueResponseFromTransactionTest(){
        assertThat(connectionData.getQueueResponseFromTransaction(keyConnect)).isNotNull();
    }

    @Test
    public void getTemplateTest(){
        assertThat(connectionData.getTemplate(keyConnect)).isNotNull();
    }

    @Test
    public void getConnectionsTest(){
        assertThat(connectionData.getConnections()).isNotNull();
    }

}
