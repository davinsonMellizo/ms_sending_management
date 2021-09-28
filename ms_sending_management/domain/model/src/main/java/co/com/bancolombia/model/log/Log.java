package co.com.bancolombia.model.log;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Log {
    private Integer id;
    private Integer documentType;
    private Long documentNumber;
    private String idAlert;
    private String alertType;
    private String alertDestination;
    private String messageType;
    private String messageSent;
    private String accountType;
    private Integer accountNumber;
    private String operationChannel;
    private String operationCode;
    private String operationDescription;
    private Integer operationNumber;
    private Long enabledAmount;
    private Integer sendResponseCode;
    private String sendResponseDescription;
    private Integer priority;
    private String template;
    private Integer alertIndicator;
    private String indicatorDescription;
    private String applicationCode;
    private LocalDateTime dateCreation;
    private String userField1;
    private String userField2;
    private Long userField3;
    private Long userField4;
    private String userField5;
    private String userField6;
}
