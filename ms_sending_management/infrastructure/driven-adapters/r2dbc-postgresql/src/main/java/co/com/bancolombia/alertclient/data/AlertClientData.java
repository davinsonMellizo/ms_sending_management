package co.com.bancolombia.alertclient.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("alert_client")
public class AlertClientData implements Persistable<String> {

    @Id
    private String idAlert;
    private Integer idClient;
    private Long documentNumber;
    @Column("id_document_type")
    private Integer documentType;
    private Integer numberOperations;
    private Long amountEnable;
    private Long accumulatedOperations;
    private Long accumulatedAmount;
    private String associationOrigin;
    private String creationUser;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime transactionDate;

    @Transient
    private String id;

    @Override
    @Transient
    public boolean isNew() {
        return this.id == null ? true : false;
    }
}
