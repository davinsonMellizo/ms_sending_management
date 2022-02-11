package co.com.bancolombia.model.message;

import co.com.bancolombia.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Sms extends Request {
    private String To;
    private String text;
    private String CustomData;
    private Boolean IsPremium;
    private Boolean IsFlash;
    private Boolean Longmessage;
    private String Url;
    private Boolean domainshorturl;
}
