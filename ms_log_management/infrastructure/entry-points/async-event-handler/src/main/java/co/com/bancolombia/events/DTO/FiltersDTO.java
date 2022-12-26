package co.com.bancolombia.events.DTO;

import co.com.bancolombia.model.log.Filters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FiltersDTO {
    private String transactionIdentifier;
    @Builder.Default
    private String documentType="";
    @Builder.Default
    private String documentNumber="";
    @Builder.Default
    private String contact="";
    @Builder.Default
    private String consumer="";
    @Builder.Default
    private String provider="";
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Mono<Filters> toModel(){
        return Mono.just(
          Filters.builder()
                  .transactionIdentifier(this.transactionIdentifier)
                  .documentNumber(this.documentNumber)
                  .documentType(this.documentType)
                  .contact(this.contact)
                  .provider(this.provider)
                  .consumer(this.consumer)
                  .startDate(this.startDate)
                  .endDate(this.endDate)
                  .build()
        );
    }
}
