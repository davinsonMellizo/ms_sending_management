package co.com.bancolombia.model.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Alert implements Serializable {
    private String id;
    private String idTransaction;
    private Integer idCategory;
    private String description;
    private String idConsumer;
    private String providerMail;
    private String providerSms;
    private String templateName;
    private String remitter;
    private Integer idState;
    private Integer priority;
    private String nature;
    private String message;
    private String push;
    private Boolean obligatory;
    private Boolean basicKit;
}
