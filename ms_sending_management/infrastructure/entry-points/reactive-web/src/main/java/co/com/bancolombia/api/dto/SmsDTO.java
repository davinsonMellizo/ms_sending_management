package co.com.bancolombia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String urlForShortening = "";

}
