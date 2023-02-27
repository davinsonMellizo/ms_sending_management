package co.com.bancolombia.api.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MessageDTO {
    @Schema(description = "Obligatorio para envío de sms cuando no se envía, " +
            "Alerta o Consumidor y código de transacción", allowableValues = {"1","2","3", "4", "5"})
    @Max(value = 5, message = "{constraint.max}")
    @Min(value = 1, message = "{constraint.min}")
    private Integer priority;
    @Builder.Default
    @Size(max = 20, message = "{constraint.size}")
    private String category = "";
    @Builder.Default
    @Schema(allowableValues = {"MAIL", "SMS", "PUSH"})
    private ArrayList<String> preferences= new ArrayList<>();
    @Schema(description = "Mapa de parametros para construir el mensaje",
            example = "{\"mensaje\": \"Actualización exitosa\"}")
    @Size(min = 1, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private Map<String, String> parameters;
    @Builder.Default
    @Size(max = 100, message = "{constraint.size}")
    @Schema(description = "Obligatorio para envío de correo cuando no se envía, " +
            "Alerta o Consumidor y código de transacción")
    private String template = "";


}
