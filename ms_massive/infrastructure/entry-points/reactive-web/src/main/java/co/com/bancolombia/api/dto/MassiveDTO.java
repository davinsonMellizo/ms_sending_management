package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.massive.Massive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MassiveDTO {

    @Size(min = 1, max = 50, message = "debe tener entre {min} y {max} caracteres de longitud")
    @NotNull(message = "no debe ser nulo")
    private String idCampaign;

    @Size(min = 1, max = 10, message = "debe tener entre {min} y {max} caracteres de longitud")
    @NotNull(message = "no debe ser nulo")
    private String idConsumer;


    public Mono<Massive> toModel() {
        return Mono.just(Massive.builder()
                .idCampaign(this.idCampaign)
                .idConsumer(this.idConsumer)
                .build());
    }
}
