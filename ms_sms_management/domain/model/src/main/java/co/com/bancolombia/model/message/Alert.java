package co.com.bancolombia.model.message;

import co.com.bancolombia.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Alert extends Request {
    private String priority;
    private To to;
    private String message;
    private Template template;
    private String url;
    private String provider;
    private String logKey;
    private String category;
    private Boolean isFlash;
    private Boolean isPremium;

}
