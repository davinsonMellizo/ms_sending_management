package co.com.bancolombia.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SMS {
    private String To; // Contacto
    private String text; // build
    private String CustomData;
    private Boolean IsPremium;
    private Boolean IsFlash;
    private Boolean Longmessage;
    private String Url;
    private Boolean domainshorturl;
}
