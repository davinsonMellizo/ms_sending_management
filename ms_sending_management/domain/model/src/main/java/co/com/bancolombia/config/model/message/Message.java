package co.com.bancolombia.config.model.message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Message {
    private Integer idOperation;
    private Integer documentType;
    private Long documentNumber;
    private String consumer;
    private String idAlert;
    private String transactionCode;
    private String value;
    private Double amount;

    private String mobile;
    private String mail;
}
