package co.com.bancolombia.events.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContactDTO {

    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
    @JsonProperty("contactChannel")
    @Schema(allowableValues = {"SMS", "MAIL", "PUSH"})
    private String contactWay;
    @NotNull(message = "{constraint.value_is_required}")
    @Size(min = 1, max = 60, message = "{constraint.size}")
    @JsonProperty("dataValue")
    @Schema(example = "+573217937584 o 3217937584 o dmellizo@bancolombia.com.co",
            description = "Valor del contacto (SMS, EMAIL o PUSH), SMS: puede enviar " +
                    "el telefono o agregar el indicativo del país junto al signo +, como se muestra en el ejemplo")
    private String value;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
    @JsonProperty("status")
    @Schema(allowableValues = {"Activo", "Inactivo"})
    private String stateContact;
    @Size(max = 10, message = "{constraint.size}")
    @Builder.Default
    @Schema(allowableValues = {"Personal", "Laboral"})
    private String environmentType = "";

}
