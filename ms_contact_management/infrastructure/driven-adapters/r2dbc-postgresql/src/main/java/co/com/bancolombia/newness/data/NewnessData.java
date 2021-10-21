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
    private String operationCode;
    private String operationDescription;
    private Boolean active;
    private String transactionCode;
    private String transactionDescription;
    private Integer numberOperations;
    private Long amountEnable;
    private String userCreation;
    private String responseCode;
    private String responseDescription;
    private Integer voucher;
    private LocalDateTime dateCreation;
    private String userField1;
    private String userField2;
    private Integer userField3;
    private Integer userField4;
}
