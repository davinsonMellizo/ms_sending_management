package co.com.bancolombia.alertclient.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("alert_client")
public class AlertClientData {

    private String idAlert;
    @ReadOnlyProperty
    private String alertDescription;
    private Long documentNumber;
    @Column("id_document_type")
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

    @Id
    @Transient
    private String id;

}
