package co.com.bancolombia.model.template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String idTemplate;
    private String messageSubject;
    private String messageBody;
    private String plainText;
}
