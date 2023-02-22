package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.message.Attachment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MailDTO {

    @Builder.Default
    @Schema(description = "Correo del cliete cuando retrieveInformation es false")
    @Size(max = 60, message = "{constraint.size}")
    private String address = "";
    @Builder.Default
    @Size(max = 60, message = "{constraint.size}")
    @Schema(description = "Obligatorio para envío de correo cuando no se envía, " +
            "Alerta o Consumidor y código de transacción")
    private String remitter = "";

    @Builder.Default
    private ArrayList<Attachment> attachments = new ArrayList<>();
}
