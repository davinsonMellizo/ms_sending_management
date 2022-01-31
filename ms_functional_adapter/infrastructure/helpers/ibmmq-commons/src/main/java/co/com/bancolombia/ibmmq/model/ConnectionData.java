package co.com.bancolombia.ibmmq.model;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_CONFIG_TRANSACTION_NOT_FOUND;

@Data
public class ConnectionData {

    private List<ConnectionDTO> connections;
    private List<QueueDto> queues;
    private List<Listener> listener;
    private Map<String, Transaction> transactions;

    public QueueDto getQueue(String nameQueue){
        return getQueues()
                .stream()
                .filter(conn -> conn.getName().equals(nameQueue))
                .findFirst()
                .orElseThrow(() -> new TechnicalException(TECHNICAL_CONFIG_TRANSACTION_NOT_FOUND));
    }

    public Listener getListener(String keyConnect){
        String idListener = getTransactions().get(keyConnect).getListener();
        return getListener()
                .stream()
                .filter(conn -> conn.getName().equals(idListener))
                .findFirst()
                .orElseThrow(() -> new TechnicalException(TECHNICAL_CONFIG_TRANSACTION_NOT_FOUND));
    }

    public void setNameQueueListener(String name,String namePrev){
        getListener().stream()
                .filter(queue -> queue.getQueueResponse().equals(namePrev))
                .forEachOrdered(queue -> queue.setQueueResponse(name));
    }

    public QueueDto getQueueFromTransaction(String keyConnect){
        return getQueue(getListener(keyConnect).getQueueRequest());
    }

    public QueueDto getQueueResponseFromTransaction(String keyConnect){
        return getQueue(getListener(keyConnect).getQueueResponse());
    }

    public String getTemplate(String trxId){
        return getTransactions().get(trxId).getTemplate();
    }

}
