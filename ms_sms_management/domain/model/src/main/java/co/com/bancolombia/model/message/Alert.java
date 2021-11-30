package co.com.bancolombia.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Alert {
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
