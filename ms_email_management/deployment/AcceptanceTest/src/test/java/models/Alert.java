package models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alert extends Request{
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
    public static class Destination{
        private String toAddress;
        private String ccAddress;
        private String bccAddress;
    }
}
