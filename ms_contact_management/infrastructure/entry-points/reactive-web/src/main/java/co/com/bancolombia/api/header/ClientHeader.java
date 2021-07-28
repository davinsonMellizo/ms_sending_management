package co.com.bancolombia.api.header;

import co.com.bancolombia.model.client.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class ClientHeader {

    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 2, message = "{constraint.size}")
    private String documentType;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number}")
    @Size(min = 1, max = 15, message = "{constraint.size}")
    private String documentNumber;

    public Mono<Client> toModel() {
        return Mono.just(Client.builder()
                .documentType((this.documentType))
                .documentNumber(Long.parseLong(this.documentNumber))
                .build());
    }
}

