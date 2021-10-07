package co.com.bancolombia.model.message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

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
    private Double amount;

    private String mobile;
    private String mail;

    private ArrayList<Parameter> parameters;

}
