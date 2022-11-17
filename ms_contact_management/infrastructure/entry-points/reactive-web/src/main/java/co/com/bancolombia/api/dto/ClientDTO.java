package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class ClientDTO {

    private ClientIdentificationDTO identification;
    @Size(max = 20, message = "{constraint.size}")
    private String keyMdm;
    @NotNull(message = "{constraint.not_null}")
    @Size(max = 10, message = "{constraint.size}")
    @JsonProperty("status")
    @Schema(allowableValues = {"Activo", "Inactivo"})
    private String stateClient;
    @Schema(allowableValues = {"true", "false"})
    private Boolean delegate;
    private TraceabilityDTO traceability;
    private List<@Valid ContactDTO> contactData;
}
