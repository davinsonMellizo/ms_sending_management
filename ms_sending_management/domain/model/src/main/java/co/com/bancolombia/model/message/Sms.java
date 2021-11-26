package co.com.bancolombia.model.message;

import co.com.bancolombia.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Sms extends Request {
    private Integer priority;
    private String to;
    private Template template;
    private String text;
    private String url;
    private String provider;
    private String documentType;
    private String documentNumber;
    private String enrolClient;
}
