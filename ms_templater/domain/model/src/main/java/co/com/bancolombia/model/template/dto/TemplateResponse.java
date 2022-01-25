package co.com.bancolombia.model.template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class  TemplateResponse {
    private String idTemplate;
    private String messageType;
    private String version;
    private String idConsumer;
    private String description;
    private String messageSubject;
    private String messageBody;
    private String plainText;
    private String creationUser;
    private String creationDate;
    private String modificationUser;
    private String modificationDate;
}
