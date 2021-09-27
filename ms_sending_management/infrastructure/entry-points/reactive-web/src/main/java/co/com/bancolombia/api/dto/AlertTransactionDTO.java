package co.com.bancolombia.api.dto;

import co.com.bancolombia.config.model.alerttransaction.AlertTransaction;
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
@Builder
public class AlertTransactionDTO {

    @Size(min = 1, max = 3, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idAlert;
    @Size(min = 1, max = 3, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idConsumer;
    @Size(min = 1, max = 4, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idTransaction;
    @Size(max = 20, message = "{constraint.size}")
    private String creationUser;

    public Mono<AlertTransaction> toModel() {
        return Mono.just(AlertTransaction.builder()
                .idAlert(this.idAlert)
                .idTransaction(this.idTransaction)
                .idConsumer(this.idConsumer)
                .creationUser(this.creationUser)
                .build());
    }
}
