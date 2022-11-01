package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    private String stateClient;
    private Boolean delegate;
    private TraceabilityDTO traceability;
}
