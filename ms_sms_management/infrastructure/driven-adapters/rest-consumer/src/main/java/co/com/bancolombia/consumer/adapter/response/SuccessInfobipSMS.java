package co.com.bancolombia.consumer.adapter.response;

import co.com.bancolombia.model.message.SMSInfobip;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SuccessInfobipSMS {
    private List<SMSInfobip.Response> messages;
}
