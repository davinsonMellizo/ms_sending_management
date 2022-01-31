package co.com.bancolombia.ibmmq.supervisor;

import co.com.bancolombia.ibmmq.exceptions.JMSExtException;
import co.com.bancolombia.ibmmq.jms.JmsManagement;
import co.com.bancolombia.model.log.LoggerBuilder;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ReconnectSupervisor implements ExceptionListener {

    private final Worker worker;
    private final ThreadPoolExecutor executorService;
    private final LoggerBuilder log;


    public ReconnectSupervisor(JmsManagement wrapper, final MessageListener listener, final LoggerBuilder log) {
        this.worker = new Worker(wrapper, listener, log);
        this.executorService = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1));
        this.log = log;
    }

    @Override
    public void onException(JMSException exception) {
        if (exception instanceof JMSExtException) {
            worker.setConnectionName(((JMSExtException) exception).getConnectionName());
        }
        log.info("Exception context listener at MQWrapperConfig" + exception.getMessage());
        if (isUnQueued() && !isRunning()) {
            log.info("Starting reconnect worker");
            executorService.execute(worker);
        } else {
            log.info("A reconnect worker is already running");
        }
    }

    protected boolean isUnQueued() {
        return executorService.getQueue().isEmpty();
    }

    protected boolean isRunning() {
        return executorService.getActiveCount() > 0;
    }
}