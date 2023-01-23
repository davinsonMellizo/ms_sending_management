package co.com.bancolombia.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Message {
    private Boolean retrieveInformation;
    private Integer documentType;
    private Long documentNumber;
    private ArrayList<String> preferences;
    private String consumer;
    private String alert;
    private String transactionCode;
    private Long amount;
    private String url;
    private String template;
    private String remitter;
    private Integer priority;
    private String applicationCode;
    private String logKey;

    private String phone;
    private String phoneIndicator;

    private String mail;

    private Boolean push;

    private Map<String, String> parameters;
    private ArrayList<Attachment> attachments;


}
