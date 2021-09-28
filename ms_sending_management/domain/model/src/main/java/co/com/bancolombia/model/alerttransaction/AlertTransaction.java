package co.com.bancolombia.model.alerttransaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AlertTransaction {
    private String idAlert;
    private String idConsumer;
    private String idTransaction;
    private String creationUser;
    private LocalDateTime createdDate;

}
