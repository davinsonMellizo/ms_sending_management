package models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alert extends Request{
    private Integer priority;
    private String to;
    private Template template;
    private String url;
    private String provider;
    private String documentType;
    private String documentNumber;
    private String enrolClient;
    private String logKey;


}
