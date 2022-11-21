package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.contact.Contact;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContactFindDTO {

    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
    @Schema(allowableValues = {"SMS", "MAIL", "PUSH"})
    private String segment;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
    @Schema(allowableValues = {"SMS", "MAIL", "PUSH"})
    private String contactChannel;
    @NotNull(message = "{constraint.value_is_required}")
    @Size(min = 1, max = 60, message = "{constraint.size}")
    @Schema(example = "+573217937584 o 3217937584 o dmellizo@bancolombia.com.co",
            description = "Valor del contacto (SMS, EMAIL o PUSH), SMS")
    private String dataValue;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
    @Schema(allowableValues = {"Activo", "Inactivo"})
    private String status;
    @Size(max = 10, message = "{constraint.size}")
    @Schema(allowableValues = {"Personal", "Laboral"})
    private String environmentType;
    @NotNull(message = "{constraint.not_null}")
    @Schema(example = "2022-11-17T12:10:53.755977")
    private LocalDateTime createdDate;
    @Schema(example = "2022-11-17T12:10:53.755977")
    private LocalDateTime modifiedDate;

    public ContactFindDTO(Contact contact) {
        this.segment = contact.getSegment();
        this.contactChannel = contact.getContactWay();
        this.dataValue = contact.getValue();
        this.status = contact.getStateContact();
        this.createdDate = contact.getCreatedDate();
        this.environmentType = contact.getEnvironmentType();
        this.modifiedDate = contact.getModifiedDate();
    }
}
