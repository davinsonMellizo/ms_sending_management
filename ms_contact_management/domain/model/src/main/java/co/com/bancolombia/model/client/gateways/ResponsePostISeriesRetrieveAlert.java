package co.com.bancolombia.model.client.gateways;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ResponsePostISeriesRetrieveAlert {

    private Long documentNumber;
    private String documentType;
    private String alertType;
    private String customerMobileNumber;
    private String customerEmail;
    private String pushActive;
    private LocalDateTime lastDataModificationDate;
}
