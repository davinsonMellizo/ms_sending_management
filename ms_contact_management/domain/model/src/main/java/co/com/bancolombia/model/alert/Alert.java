package co.com.bancolombia.model.alert;

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
public class Alert {
    private Integer operation;
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
    private String logKey;

    private String phone;
    private String phoneIndicator;

    private String mail;


    private List<Parameter> parameters;


}
