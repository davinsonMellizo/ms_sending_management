package co.com.bancolombia.consumer.adapter.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
