package co.com.bancolombia.alerttransaction.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("alert_transaction")
public class AlertTransactionData{

    private String idAlert;
    private String idConsumer;
    private String idTransaction;
    private String creationUser;
    private LocalDateTime createdDate;

    @Id
    @Transient
    private String id;

}
