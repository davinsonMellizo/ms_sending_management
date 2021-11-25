package co.com.bancolombia.model.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Log {
    private Integer id;
    private String documentType;
    private Long documentNumber;
    private String logType;
    private String medium;
    private String contact;
    private String messageSent;
    private String consumer;
    private String alertId;
    private String alertDescription;
    private String transactionId;
    private Long amount;
    private Integer responseCode;
    private String responseDescription;
    private Integer priority;
    private String provider;
    private String template;
    private Integer operationId;
    private String operationDescription;
    private LocalDateTime dateCreation;
}
