package co.com.bancolombia.model.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class QueryLog {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime referenceDate;
    private String documentNumber;
    private String documentType;
    private String contactValue;
    private String consumer;
    private String provider;
}
