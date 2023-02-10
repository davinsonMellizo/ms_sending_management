package co.com.bancolombia.model.message;

import co.com.bancolombia.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Alert extends Request {
    private String priority;
    private To to;
    private String message;
    private Template template;
    private String urlForShortening;
    private String provider;
    private String category;
    private String logKey;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class To implements Serializable {
        private String phoneNumber;
        private String phoneIndicator;
    }
}
