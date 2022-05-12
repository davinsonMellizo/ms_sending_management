package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class IdentificationDTO {

    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 2, message = "{constraint.size}")
    @JsonProperty("type")
    private String documentType;
    @Max(value = 999999999999999L, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    @JsonProperty("number")
    private Long documentNumber;
}
