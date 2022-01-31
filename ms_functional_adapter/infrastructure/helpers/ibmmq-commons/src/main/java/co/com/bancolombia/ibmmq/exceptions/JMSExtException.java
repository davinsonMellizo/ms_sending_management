package co.com.bancolombia.ibmmq.exceptions;

import lombok.Getter;

import javax.jms.JMSException;

@Getter
public class JMSExtException extends JMSException {

    private String connectionName;

    public JMSExtException(String message, String connectionName) {
        super(message);
        this.connectionName = connectionName;
    }
}
