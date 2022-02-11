package co.com.bancolombia.ibmmq.jms;

import javax.jms.JMSProducer;
import java.util.HashMap;
import java.util.Map;

public class ProducerManagement {
    private final Map<String, JMSProducer> producers;
    private final Map<String, ConnectionJms> connectionsMap;

    public ProducerManagement(final Map<String, ConnectionJms> connectionsMap) {
        producers = new HashMap<>();
        this.connectionsMap = connectionsMap;
    }

    public JMSProducer getProducer(String connectionName) {
        if (!producers.containsKey(connectionName)) {
            producers.put(connectionName, connectionsMap.get(connectionName).getContext().createProducer());
        }
        return producers.get(connectionName);
    }

    public void removeProducer(String connectionName) {
        producers.remove(connectionName);
    }
}