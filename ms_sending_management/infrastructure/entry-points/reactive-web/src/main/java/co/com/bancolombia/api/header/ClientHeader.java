package co.com.bancolombia.api.header;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class ClientHeader {

    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private String documentType;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private String documentNumber;

   /* public Mono<Client> toModel() {
        return Mono.just(Client.builder()
                .documentType(Integer.parseInt(this.documentType))
                .documentNumber(Long.parseLong(this.documentNumber))
                .build());
    }*/
}

