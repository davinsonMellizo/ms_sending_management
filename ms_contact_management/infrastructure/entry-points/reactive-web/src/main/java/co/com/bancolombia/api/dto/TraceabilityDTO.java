package co.com.bancolombia.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TraceabilityDTO {

    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 3, message = "{constraint.size}")
    private String consumerCode;
    @Size(max = 20, message = "{constraint.size}")
    @Builder.Default
    private String creationUser = "";
}
