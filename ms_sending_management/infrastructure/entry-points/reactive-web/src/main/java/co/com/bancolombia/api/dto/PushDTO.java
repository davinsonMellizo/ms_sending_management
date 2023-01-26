package co.com.bancolombia.api.dto;

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
public class PushDTO {
    @Builder.Default
    @Schema(description = "Aplicación a la que se envía el push")
    @Size(max = 30, message = "{constraint.size}")
    private String applicationCode = "";
}
