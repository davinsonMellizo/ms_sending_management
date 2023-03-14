package co.com.bancolombia.consumer.adapter.response;

import co.com.bancolombia.model.message.SMSInfobip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorTokenInfobipRequest {

    private String Error;
    private SMSInfobip.RequestError requestError;

}
