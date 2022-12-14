package co.com.bancolombia.model.message;

import co.com.bancolombia.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class SMSMasiv extends Request {
    private String to;
    private String text;
    private String customData;
    private Boolean isPremium;
    private Boolean isFlash;
    private Boolean isLongmessage;
    private transient ShortUrlConfig shortUrlConfig;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class ShortUrlConfig{
        private String url;
        private String domainshorturl;
    }
}
