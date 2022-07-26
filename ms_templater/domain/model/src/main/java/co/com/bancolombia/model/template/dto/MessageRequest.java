package co.com.bancolombia.model.template.dto;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder(toBuilder = true)
public class MessageRequest {
    private String idTemplate;
    private Map<String, String> messageValues;
}
