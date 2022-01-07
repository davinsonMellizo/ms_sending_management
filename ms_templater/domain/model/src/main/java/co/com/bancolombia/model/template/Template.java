package co.com.bancolombia.model.template;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Template {
    private String id;
    private String messageType;
    private String messageSubject;
    private String messageBody;
    private String message;
    private String creationUser;
    private String creationDate;
}
