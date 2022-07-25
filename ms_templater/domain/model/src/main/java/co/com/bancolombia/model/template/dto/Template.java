package co.com.bancolombia.model.template.dto;

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
public class Template {
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
    private String status;

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        Template fobj = (Template) obj;
        return idTemplate.equals(fobj.getPlainText());
    }

    @Override
    public int hashCode() {
        final int prime = Constants.PRIME;
        int result = Constants.ONE;
        return prime * result + ((idTemplate == null) ? Constants.ZERO : idTemplate.hashCode());
    }
}
