package co.com.bancolombia.model.template.dto;

import co.com.bancolombia.Request;
import co.com.bancolombia.commons.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder(toBuilder = true)
public class TemplateRequest extends Request {
    private String MessageType;
    private String IdTemplate;
    private String Version;
    private String IdConsumer;
    private String Description;
    private String MessageSubject;
    private String MessageBody;
    private String PlainText;
    private String CreationUser;
    private String ModificationUser;

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        TemplateRequest fobj = (TemplateRequest) obj;
        return PlainText.equals(fobj.getPlainText());
    }

    @Override
    public int hashCode() {
        final int prime = Constants.PRIME;
        int result = Constants.ONE;
        return prime * result + ((PlainText == null) ? Constants.ZERO : PlainText.hashCode());
    }
}
