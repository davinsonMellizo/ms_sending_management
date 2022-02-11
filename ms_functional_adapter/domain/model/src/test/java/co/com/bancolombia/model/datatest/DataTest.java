package co.com.bancolombia.model.datatest;

public class DataTest {

    public static String configResource = "{\"data\":[{\"queryName\":\"transactions.mq.9369\",\"channel\":\"ALM\"," +
            "\"transaction\":\"9369\",\"template\":\"<#assign body = JsonUtil.jsonToMap(input)>${body.nro}  Mi nombre es ${body.name}\"},{\"queryName\":\"transactions.mq.9610\",\"channel\":\"ALM\",\"transaction\":\"9610\",\"template\":\"<#assign body = JsonUtil.jsonToMap(input)>${body.nro}  Mi nombre es ${body.name}\"}]}";

    public static String config = "{\"connections\":[{\"name\":\"connFactory1\",\"qmGroup\":\"QM1\",\"secret\":\"connection1\"}],\"queues\":[{\"name\":\"DEV.QUEUE.1\",\"connection\":\"connFactory1\"},{\"name\":\"DEV.QUEUE.2\",\"connection\":\"connFactory1\"},{\"name\":\"DEV.QUEUE.3\",\"connection\":\"connFactory1\"}],\"listener\":[{\"name\":\"id1\",\"queueRequest\":\"DEV.QUEUE.1\",\"queueResponse\":\"DEV.QUEUE.2\"},{\"name\":\"id2\",\"queueRequest\":\"DEV.QUEUE.3\",\"queueResponse\":\"DEV.QUEUE.2\"}],\"transactions\":{\"ALM9610\":{\"listener\":\"id1\",\"template\":\"{ \\\"name\\\" : \\\"${payload?substring(5, 20)}\\\"}\"},\"ALM9369\":{\"listener\":\"id1\",\"template\":\"{ \\\"name\\\" : \\\"${payload?substring(5, 25)}\\\"}\"},\"APP9369\":{\"listener\":\"id2\",\"template\":\"{ \\\"name\\\" : \\\"${payload?substring(0, 20)}\\\"}\"}}}";

    public static String QueueDto = "{\"name\":\"DEV.QUEUE.1\",\"connection\":\"connFactory1\"}";
}
