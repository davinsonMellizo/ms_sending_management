package co.com.bancolombia.consumer.adapter.response;

import co.com.bancolombia.model.message.SMSInfobip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SuccessInfobipSMS {
    private List<SMSInfobip.Response> messages;
}
