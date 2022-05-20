package co.com.bancolombia.ibmmq.model;

import lombok.Data;

@Data
public class QueueDto {
    private String name;
    private String connection;
    private boolean temporary;

    public void setNameTemporary(String name, ConnectionData conn) {
        conn.getListener().stream()
                .filter(queue -> queue.getQueueResponse().equals(this.name))
                .peek(queue -> queue.setQueueResponse(name));
        setName(name);
    }
}
