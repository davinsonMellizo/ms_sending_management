package co.com.bancolombia.model.message;

import co.com.bancolombia.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Alert extends Request {
    private String priority;
    private String provider;
    private String from;
    private Destination destination;
    private List<Attachment> attachments;
    private Template template;
    private Message message;
    private String category;
    private String logKey;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class Destination{
        private String toAddress;
        private String ccAddress;
        private String bccAddress;
    }

}
