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
    private String IdTemplate;
    private String MessageType;
    private String Version;
    private String IdConsumer;
    private String Description;
    private String MessageSubject;
    private String MessageBody;
    private String PlainText;
    private String CreationUser;
    private String CreationDate;
    private String ModificationUser;
    private String ModificationDate;
}
