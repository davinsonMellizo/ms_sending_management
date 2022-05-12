package co.com.bancolombia.model.message;

import co.com.bancolombia.Request;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
public class Sms extends Request {
    private String to;
    private String text;
    private String customData;
    private Boolean isPremium;
    private Boolean isFlash;
    private Boolean isLongmessage;
    private ShortUrlConfig shortUrlConfig;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class ShortUrlConfig{
        private String url;
        private String domainshorturl;
    }
}
