package co.com.bancolombia.consumer.adapter.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SuccessMasivianSMS {
    private String statusCode;
    private String statusMessage;
    private String messageId;
}
