package co.com.bancolombia.model.alertclient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AlertClient {

    private String idAlert;
    private String alertDescription;
    private Long documentNumber;
    private Integer documentType;
    private Integer numberOperations;
    private Long amountEnable;
    private Integer accumulatedOperations;
    private Long accumulatedAmount;
    private String associationOrigin;
    private String creationUser;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime transactionDate;

}
