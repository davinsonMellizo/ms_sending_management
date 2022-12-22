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
public class Filters {
    private String transactionIdentifier;
    private String documentType;
    private String documentNumber;
    private String contact;
    private String consumer;
    private String provider;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
