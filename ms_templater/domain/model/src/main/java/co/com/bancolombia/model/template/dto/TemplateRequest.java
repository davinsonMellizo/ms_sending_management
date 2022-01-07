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
    private String id;
    private String messageType;
    private String messageSubject;
    private String messageBody;
    private String messageText;
    private String creationUser;
    private String consumerId;

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        TemplateRequest fobj = (TemplateRequest) obj;
        return messageText.equals(fobj.getMessageText());
    }

    @Override
    public int hashCode() {
        final int prime = Constants.PRIME;
        int result = Constants.ONE;
        return prime * result + ((messageText == null) ? Constants.ZERO : messageText.hashCode());
    }
}
