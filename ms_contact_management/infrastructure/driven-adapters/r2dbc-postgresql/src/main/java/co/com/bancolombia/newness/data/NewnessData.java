package co.com.bancolombia.newness.data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@Table("newness")
public class NewnessData {
    @Id
    private Integer id;
    private Integer documentType;
    private Long documentNumber;
    private String contact;
    private String channelTransaction;
    private String idAlert;
    private String descriptionAlert;
    private Boolean active;
    private String transactionDescription;
    private Integer numberOperations;
    private Long amountEnable;
    private String userCreation;
    private String responseCode;
    private String responseDescription;
    private Long voucher;
    private LocalDateTime dateFirstInscription;
    private LocalDateTime dateCreation;
}
