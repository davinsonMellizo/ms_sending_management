package co.com.bancolombia.model.message;

import co.com.bancolombia.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Mail extends Request {
    private String provider;
    private String from;
    private Destination destination;
    private List<Attachment> attachments;
    private Template template;
    private String logKey;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class Destination implements Serializable {
        private String toAddress;
        private String ccAddress;
        private String bccAddress;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class Message implements Serializable{
        private String subject;
        private String body;
    }


}
