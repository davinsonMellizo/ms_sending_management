package co.com.bancolombia.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AlertParametersDTO {

    @Builder.Default
    @Size(max = 10, message = "constraint.size")
    private String consumer = "";
    @Builder.Default
    @Size(max = 3, message = "constraint.size")
    private String alert = "";
    @Builder.Default
    @Size(max = 4, message = "constraint.size")
    private String transactionCode = "";
    @Builder.Default
    @Max(value = 999999999999999999L, message = "constraint.max")
    private Long amount = 0L;

}
