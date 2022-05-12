package co.com.bancolombia.model.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class ResponseUpdateClient {

    private String idResponse;
    private String description;
    private String voucherNumber;

}
