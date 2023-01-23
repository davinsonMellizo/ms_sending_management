package co.com.bancolombia.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MessageDTO {
    @Builder.Default
    private String category = "";
    @Builder.Default
    private ArrayList<String> preferences= new ArrayList<>();
    @Builder.Default
    private Map<String, String> parameters = new HashMap<>();
    @Builder.Default
    private String template = "";
    @Builder.Default
    private SmsDTO sms = new SmsDTO();
    @Builder.Default
    private MailDTO mail = new MailDTO();
    @Builder.Default
    private PushDTO push = new PushDTO();

}
