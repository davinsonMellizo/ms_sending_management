package co.com.bancolombia.model.transaction;

import co.com.bancolombia.model.message.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class Transaction {

    private String messageId;
    private String correlationID;
    private String channel;
    private String nroTransaction;
    private String template;
    private Object payload;
    private Map<String, Object> data;
    private Message.From from;

    public Transaction data(Map<String, Object> data) {
        this.setData(data);
        return this;
    }

}
