package co.com.bancolombia.model.message;


import co.com.bancolombia.Request;
import lombok.*;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
public class SMSInfobip extends Request {

    private @Singular List<Message> messages;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Message{
        private String from;
        private @Singular List<Destination> destinations;
        private String text;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Destination{
        private String to;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Response{
        private String messageId;
        private Status status;
        private String to;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Status {
        private String description;
        private String groupId;
        private String groupName;
        private String id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class RequestError{
        private ServiceException serviceException;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class ServiceException{
        private String messageId;
        private String text;
    }
}
