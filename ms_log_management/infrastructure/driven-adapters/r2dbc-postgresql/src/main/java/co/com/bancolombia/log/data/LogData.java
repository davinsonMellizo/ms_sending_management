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
    private String template;
    private Integer operationId;
    private String operationDescription;
    private LocalDateTime dateCreation;
}
