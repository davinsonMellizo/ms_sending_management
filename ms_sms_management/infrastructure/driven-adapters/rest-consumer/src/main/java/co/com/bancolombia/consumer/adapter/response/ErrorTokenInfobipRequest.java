package co.com.bancolombia.consumer.adapter.response;

import co.com.bancolombia.model.message.SMSInfobip;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorTokenInfobipRequest {

    public String Error;
    private SMSInfobip.RequestError requestError;

}
