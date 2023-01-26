package co.com.bancolombia.api.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MessageDTO {
    @Builder.Default
    @Size(max = 20, message = "{constraint.size}")
    private String category = "";
    @Builder.Default
    @Schema(allowableValues = {"MAIL", "SMS"})
    private ArrayList<String> preferences= new ArrayList<>();
    @Builder.Default
    @Schema(description = "Mapa de parametros para construir el mensaje",
            example = "{\"mensaje\": \"Actualización exitosa\"}")
    @ArraySchema(minItems = 1)
    private Map<String, String> parameters = new HashMap<>();
    @Builder.Default
    private SmsDTO sms = new SmsDTO();
    @Builder.Default
    private MailDTO mail = new MailDTO();
    @Builder.Default
    @Schema(description = "Para el envío de push es obligatorio enviar la identificación del cliente")
    private PushDTO push = new PushDTO();

}
