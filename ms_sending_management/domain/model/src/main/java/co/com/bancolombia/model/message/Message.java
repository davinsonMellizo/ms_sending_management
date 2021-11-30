package co.com.bancolombia.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Message {
    private Integer operation;
    private Integer documentType;
    private Long documentNumber;
    private String consumer;
    private String alert;
    private String transactionCode;
    private Long amount;
    private String url;
    private String template;
    private String logKey;

    private String phone;
    private String phoneIndicator;

    private String mail;

    private Boolean push;

    private List<Parameter> parameters;
    private ArrayList<Attachment> attachments;

}
