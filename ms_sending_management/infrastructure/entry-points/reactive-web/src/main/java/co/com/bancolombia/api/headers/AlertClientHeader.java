package co.com.bancolombia.api.headers;

import co.com.bancolombia.model.alertclient.AlertClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class AlertClientHeader {

    @Size(max = 2, message = "{constraint.maximum_length}")
    private String idAlert;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    @Size(max = 2, message = "{constraint.maximum_length}")
    private String documentType;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    @Size(max = 15, message = "{constraint.maximum_length}")
    private String documentNumber;

    public Mono<AlertClient> toModel() {
        return Mono.just(AlertClient.builder()
                .idAlert(this.idAlert)
                .idDocumentType(Integer.parseInt(this.documentType))
                .documentNumber(Long.parseLong(this.documentNumber))
                .build());
    }
}

