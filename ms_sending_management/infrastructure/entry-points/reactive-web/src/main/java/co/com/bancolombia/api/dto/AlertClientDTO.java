package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.alertclient.AlertClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertClientDTO {

    @Size(min = 1, max = 3, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idAlert;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Long documentNumber;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    @Max(value = 99, message = "{constraint.maximum_value}")
    private Integer idDocumentType;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Integer numberOperations;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Long amountenable;
    private Long accumulatedOperations;
    private Long accumulatedAmount;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 3, message = "{constraint.size}")
    private String associationOrigin;
    private String creationUser;

    public Mono<AlertClient> toModel() {
        return Mono.just(AlertClient.builder()
                .idAlert(this.idAlert)
                .documentNumber(this.documentNumber)
                .idDocumentType(this.idDocumentType)
                .numberOperations(this.numberOperations)
                .amountEnable(this.amountenable)
                .accumulatedOperations(this.accumulatedOperations)
                .accumulatedAmount(this.accumulatedAmount)
                .associationOrigin(this.associationOrigin)
                .creationUser(this.creationUser)
                .build());
    }

}
