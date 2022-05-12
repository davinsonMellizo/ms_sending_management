package co.com.bancolombia.model.message;

import co.com.bancolombia.Request;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
public class SMSInalambria extends Request {
    private LocalDateTime DateMessage;
    private String Devices;
    private Integer FlashSMS;
    private Integer HasMore;
    private String MessageData;
    private String MessagePattern;
    private String MessageText;
    private Integer TemplateId;
    private Integer TransactionNumber;
    private Integer Type;
    private String Url;
}
