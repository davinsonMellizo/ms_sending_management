package co.com.bancolombia.ibmmq.model;

import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.model.datatest.DataTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QueueDtoTest {
    private static final ConnectionData connectionData = JsonUtils.stringToType(DataTest.config, ConnectionData.class);
    private static final String queueName = "DEV.QUEUE.1";
    private static final QueueDto queueDto = new QueueDto();

    @Test
    void setNameTemporaryTest() {
        try
        {
            queueDto.setNameTemporary(queueName,connectionData);
        }
        catch (Exception ex){
            Assertions.fail(ex.getMessage());

        }
    }

}
