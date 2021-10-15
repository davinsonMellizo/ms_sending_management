package co.com.bancolombia.model.newness;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Newness {
    private Integer id;
    private Integer documentType;
    private Long document_number;
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
