package co.com.bancolombia.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class IdentificationDTO {
    @Max(value = 99, message = "{constraint.max}")
    private Integer documentType;
    @Min(value = 0, message = "{constraint.min}")
    @Max(value = 9999999999999999L, message = "{constraint.max}")
    private Long documentNumber;
}
