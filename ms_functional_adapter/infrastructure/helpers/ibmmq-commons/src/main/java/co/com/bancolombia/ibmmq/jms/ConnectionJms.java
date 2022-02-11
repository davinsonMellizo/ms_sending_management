package co.com.bancolombia.ibmmq.jms;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSContext;
import javax.jms.JMSException;

@Getter
@RequiredArgsConstructor
public class ConnectionJms {
    private final JMSContext context;
    private final Connection connection;

    public ConnectionJms(final JmsConnectionFactory factory, final ExceptionListener exceptionListener) throws JMSException {
        connection = factory.createConnection();
        connection.setExceptionListener(exceptionListener);
        context = factory.createContext();
        context.setExceptionListener(exceptionListener);
    }
}