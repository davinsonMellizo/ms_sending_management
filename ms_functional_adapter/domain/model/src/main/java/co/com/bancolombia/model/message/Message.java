package co.com.bancolombia.model.message;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String messageId;
    private String channel;
    private String transaction;
    private From from;
    private String transactionTracker;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class From {
        private String replyID;
        private String correlationID;
    }
}
