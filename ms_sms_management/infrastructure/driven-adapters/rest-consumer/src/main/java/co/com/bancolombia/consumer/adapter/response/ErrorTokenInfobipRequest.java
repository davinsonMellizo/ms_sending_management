package co.com.bancolombia.consumer.adapter.response;

import co.com.bancolombia.model.message.SMSInfobip;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorTokenInfobipRequest {
    private String Error;
    private serviceException requestError;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class serviceException{
        private String messageId;
        private String text;
    }
}
