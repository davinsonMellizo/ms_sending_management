package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String contactWay;
    @NotNull(message = "{constraint.value_is_required}")
    @Size(min = 1, max = 60, message = "{constraint.size}")
    @JsonProperty("dataValue")
    private String value;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
    @JsonProperty("status")
    private String stateContact;

}
