package co.com.bancolombia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContactsDTO {
    @Builder.Default
    private SmsDTO sms = new SmsDTO();
    @Builder.Default
    private MailDTO mail = new MailDTO();
    @Builder.Default
    @Schema(description = "Para el envío de push es obligatorio enviar la identificación del cliente")
    private PushDTO push = new PushDTO();

}
