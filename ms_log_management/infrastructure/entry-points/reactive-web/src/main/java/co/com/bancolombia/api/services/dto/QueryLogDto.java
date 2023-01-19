package co.com.bancolombia.api.services.dto;

import co.com.bancolombia.model.log.QueryLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class QueryLogDto {

    private LocalDateTime startDate;
    @Builder.Default
    private LocalDateTime endDate = LocalDateTime.now();
    private LocalDateTime referenceDate;
    private String documentNumber;
    private String documentType;
    private String contactValue;
    private String consumer;
    private String provider;


    public Mono<QueryLog> toModel() {
        return Mono.just(QueryLog.builder()
                .documentType((this.documentType))
                .documentNumber(this.documentNumber)
                .contactValue(this.contactValue)
                .referenceDate(this.referenceDate)
                .endDate(this.endDate)
                .startDate(this.startDate)
                .consumer(this.consumer)
                .provider(this.provider)
                .build());
    }
}
