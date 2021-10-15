package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.alertclient.AlertClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertClientDTO {

    @Size(min = 1, max = 3, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idAlert;
    @Max(value = 999999999999999L, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Long documentNumber;
    @NotNull(message = "{constraint.not_null}")
    @Min(value = 0, message = "{constraint.min}")
    @Max(value = 999, message = "{constraint.maximum_value}")
    private Integer documentType;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Integer numberOperations;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Long amountEnable;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 3, message = "{constraint.size}")
    private String associationOrigin;
    private String creationUser;

    public Mono<AlertClient> toModel() {
        return Mono.just(AlertClient.builder()
                .idAlert(this.idAlert)
                .documentNumber(this.documentNumber)
                .documentType(this.documentType)
                .numberOperations(this.numberOperations)
                .amountEnable(this.amountEnable)
                .associationOrigin(this.associationOrigin)
                .creationUser(this.creationUser)
                .build());
    }

}
