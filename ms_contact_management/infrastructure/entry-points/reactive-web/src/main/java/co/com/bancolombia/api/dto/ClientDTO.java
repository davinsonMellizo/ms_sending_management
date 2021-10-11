package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.client.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class ClientDTO {

    @Max(value = 999999999999999L, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Long documentNumber;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 2, message = "{constraint.size}")
    private String documentType;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 20, message = "{constraint.size}")
    private String keyMdm;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 3, message = "{constraint.size}")
    private String enrollmentOrigin;
    @NotNull(message = "{constraint.not_null}")
    @Min(value = 0, message = "{constraint.min}")
    @Max(value = 9, message = "{constraint.max}")
    private Integer idState;
    @NotNull(message = "{constraint.not_null}")
    @Size(max = 20, message = "{constraint.size}")
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
