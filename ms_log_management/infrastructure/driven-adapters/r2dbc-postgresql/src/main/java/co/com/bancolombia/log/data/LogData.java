package co.com.bancolombia.log.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("log")
public class LogData {
    @Id
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
