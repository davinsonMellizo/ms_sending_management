package co.com.bancolombia.ibmmq.supervisor;

import co.com.bancolombia.ibmmq.jms.JmsManagement;
import co.com.bancolombia.ibmmq.model.ConnectionData;
import co.com.bancolombia.ibmmq.model.Listener;
import co.com.bancolombia.model.log.LoggerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.jms.JMSRuntimeException;
import javax.jms.MessageListener;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Worker implements Runnable {
    private static final int SLEEP_TIME = 200;
    private final JmsManagement wrapper;
    private final MessageListener listener;
    private final LoggerBuilder log;
    private Date startedAt;

    @Setter
    private String connectionName;

    @Override
    public void run() {
        startedAt = new Date();
        do {
            log.info("Trying to reconnect");
        } while (!connected());
    }

    private boolean connected() {
        try {
            wrapper.resetConnection(connectionName);
            resumeListener();
            log.info(String.format("Successfully reconnected after %d ms", (new Date()).getTime() - startedAt.getTime()));
            return true;
        } catch (JMSRuntimeException e) {
            log.info("Reconnecting has failed" + e.getMessage());
            waitToRetry();
            return false;
        }
    }

    private void waitToRetry() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            log.info("Cannot sleep the thread" + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void resumeListener() {
        ConnectionData connData = wrapper.getConnectionData();
        List<String> listListen = connData.getListener()
                .stream()
                .map(Listener::getQueueResponse)
                .collect(Collectors.toList());

        connData.getQueues().stream()
                .filter(queue -> queue.getConnection().equals(connectionName) && listListen.contains(queue.getName()))
                .forEach(queue -> wrapper.setMessageListener(queue, listener));
    }
}