package co.com.bancolombia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SmsDTO {

    @Builder.Default
    @Size(max = 60, message = "{constraint.size}")
    private String phone = "";
    @Builder.Default
    @Size(max = 10, message = "{constraint.size}")
    @Schema(description = "Indicador de pais del telefono", example = "+57")
    private String phoneIndicator = "";
    @Builder.Default
    @Size(max = 500, message = "{constraint.size}")
    @Schema(description = "url que se adjunta al mensaje de texto")
    private String url = "";
    @Schema(description = "Obligatorio para envío de sms cuando no se envía, " +
            "Alerta o Consumidor y código de transacción", allowableValues = {"1","2","3", "4", "5"})
    @Max(value = 5, message = "{constraint.max}")
    @Min(value = 1, message = "{constraint.min}")
    private Integer priority;
}
