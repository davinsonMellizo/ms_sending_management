package models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alert extends Request {
    private Integer priority;
    private String to;
    private String message;
    private String url;
    private String provider;
    private String logKey;


}
