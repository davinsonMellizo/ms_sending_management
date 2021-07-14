package co.com.bancolombia.model.message;
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
    private String transactionCode;
    private String value;

    private String mobile;
    private String mail;
}
