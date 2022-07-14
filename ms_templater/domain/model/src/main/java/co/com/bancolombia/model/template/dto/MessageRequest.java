package co.com.bancolombia.model.template.dto;

import co.com.bancolombia.Request;
import co.com.bancolombia.commons.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder(toBuilder = true)

public class MessageRequest extends Request {
    private String idTemplate;
    private String messageValues;

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        TemplateRequest fobj = (TemplateRequest) obj;
        return idTemplate.equals(fobj.getPlainText());
    }

    @Override
    public int hashCode() {
        final int prime = Constants.PRIME;
        int result = Constants.ONE;
        return prime * result + ((idTemplate == null) ? Constants.ZERO : idTemplate.hashCode());
    }
}
