package co.com.bancolombia.model.bridge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Bridge {

    private String documentType;
    private String documentNumber;
    private String channel;
    private String valueSms;
    private String valueEmail;
    private String valuePush;
    private String stateSms;
    private String stateEmail;
    private String statePush;
    private String voucher;
    private String date;
    private String creationUser;
}
