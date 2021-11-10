package co.com.bancolombia.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SMSInalambria {
    private LocalDateTime DateMessage;
    private String Devices;
    private Integer FlashSMS;
    private Integer HasMore;
    private String MessageData;
    private String MessagePattern;
    private String MessageText;
    private Integer TemplateId;
    private Integer TransactionNumber;
    private String Type;
    private String Url;
}
