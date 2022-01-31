package co.com.bancolombia.ibmmq.jms;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.ibmmq.model.ConnectionData;
import co.com.bancolombia.ibmmq.model.QueueDto;
import com.ibm.mq.jms.MQQueue;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static com.ibm.msg.client.wmq.common.CommonConstants.WMQ_READ_AHEAD_ALLOWED_ENABLED;

public class QueueManagement {

    private final ConcurrentMap<String, ConnectionJms> connectionsMap;
    private ConcurrentMap<String, Destination> queues;
    private final ConnectionData data;

    public QueueManagement(ConcurrentMap<String, ConnectionJms> connectionsMap, ConnectionData data) {
        this.connectionsMap = connectionsMap;
        this.data = data;
        this.queues = new ConcurrentHashMap<>();
        this.queues = this.data.getQueues()
                .stream()
                .collect(Collectors.toConcurrentMap(QueueDto::getName,this::getQueue));
    }

    public void initQueues(String connectionName){
        removeQueue(connectionName);
        this.queues = this.data.getQueues()
                .stream()
                .filter(queueDto -> queueDto.getConnection().equals(connectionName))
                .collect(Collectors.toConcurrentMap(QueueDto::getName,this::getQueue));
    }

    public Destination getQueue(QueueDto dto) {
        if (!queues.containsKey(dto.getName())) {
            Destination destination = createQueue(dto);
            queues.put(dto.getName(), destination);
        }
        return queues.get(dto.getName());
    }

    public void removeQueue(String connectionName) {
        queues.keySet().removeAll(data.getQueues().stream()
                .filter(queue -> queue.getConnection().equals(connectionName))
                .map(QueueDto::getName).collect(Collectors.toList()));
    }

    private Destination createQueue(QueueDto dto) {
        if(dto.isTemporary()){
            MQQueue queue = createTemporaryQueue(connectionsMap.get(dto.getConnection()).getConnection());
            data.setNameQueueListener(queue.getQueueName(),dto.getName());
            dto.setName(queue.getQueueName());
            return queue;
        }else{
            return createQueue(connectionsMap.get(dto.getConnection()).getContext(),dto.getName());
        }
    }

    private Destination createQueue(JMSContext context, String queueName) {
        MQQueue queue = (MQQueue) context.createQueue(queueName);
        queue.setProperty("MQMDWriteEnabled", "true");
        queue.setProperty("MQMDReadEnabled", "true");
        queue.setProperty("targetClient", "1");
        return queue;
    }

    private MQQueue createTemporaryQueue(Connection connection) {
        try {
            Session session = connection.createSession();
            MQQueue queue = (MQQueue) session.createTemporaryQueue();
            queue.setReadAheadAllowed(WMQ_READ_AHEAD_ALLOWED_ENABLED);
            return queue;
        } catch (JMSException e) {
            throw new TechnicalException(e, TechnicalExceptionEnum.TECHNICAL_JMS_ERROR);
        }
    }
}