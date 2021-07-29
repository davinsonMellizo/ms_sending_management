package co.com.bancolombia.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Mail {
    private String remitter;
    private String recipient;
    private String affair;
    private String template;
    private String typeMessage;
    private String nameParameter;
    private String typeParameter;
    private String valueParameter;
    private String value;

}
