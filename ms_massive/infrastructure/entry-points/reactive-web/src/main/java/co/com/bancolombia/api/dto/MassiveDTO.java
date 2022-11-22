package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.massive.Massive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MassiveDTO {

    @Size(min = 1, max = 50, message = "The field must be between {min} and {max} characters long")
    @NotNull(message = "The field mustn't be null")
    private String idCampaign;

    @Size(min = 1, max = 10, message = "The field must be between {min} and {max} characters long")
    @NotNull(message = "The field mustn't be null")
    private String idConsumer;

    @Min(value = 1, message = "The field must be greater than {value}")
    @NotNull(message = "The field mustn't be null")
    private Integer numberOfRecords;


    public Mono<Massive> toModel() {
        return Mono.just(Massive.builder()
                .idCampaign(this.idCampaign)
                .idConsumer(this.idConsumer)
                .numberOfRecords(this.numberOfRecords)
                .build());
    }

}
