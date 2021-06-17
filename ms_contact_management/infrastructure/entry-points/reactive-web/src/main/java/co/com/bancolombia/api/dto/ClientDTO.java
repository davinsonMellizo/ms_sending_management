package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.client.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class ClientDTO {

    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Long documentNumber;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    @Max(value = 99, message = "{constraint.maximum_value}")
    private Integer documentType;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @Size(max = 20, message = "{constraint.maximum_length}")
    private String keyMdm;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @Size(max = 3, message = "{constraint.maximum_length}")
    private String enrollmentOrigin;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    @Max(value = 9, message = "{constraint.maximum_value}")
    private Integer idState;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @Size(max = 20, message = "{constraint.maximum_length}")
    private String creationUser;

    public Mono<Client> toModel() {
        return Mono.just(co.com.bancolombia.model.client.Client.builder()
                .documentType(this.documentType)
                .documentNumber(this.documentNumber)
                .keyMdm(this.keyMdm)
                .enrollmentOrigin(this.enrollmentOrigin)
                .idState(this.idState)
                .creationUser(this.creationUser)
                .build());
    }
}
