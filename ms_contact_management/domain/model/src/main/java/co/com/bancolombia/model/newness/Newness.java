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
    private Boolean previous;
    private LocalDateTime dateFirstInscription;
    private LocalDateTime dateCreation;
}
