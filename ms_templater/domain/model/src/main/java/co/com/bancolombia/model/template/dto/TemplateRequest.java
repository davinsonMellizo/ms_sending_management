package co.com.bancolombia.model.template.dto;

import co.com.bancolombia.Request;
import co.com.bancolombia.commons.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder(toBuilder = true)
public class TemplateRequest extends Request {
    private String idTemplate;
    private String messageType;
    private String version;
    private String idConsumer;
    private String description;
    private String messageSubject;
    private String messageBody;
    private String plainText;
    private String creationUser;
    @Builder.Default
    private String creationDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    private String modificationUser;
    @Builder.Default
    private String modificationDate = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        TemplateRequest fobj = (TemplateRequest) obj;
        return plainText.equals(fobj.getPlainText());
    }

    @Override
    public int hashCode() {
        final int prime = Constants.PRIME;
        int result = Constants.ONE;
        return prime * result + ((plainText == null) ? Constants.ZERO : plainText.hashCode());
    }
}
