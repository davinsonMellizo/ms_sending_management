package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.alerttemplate.AlertTemplate;
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
public class AlertTemplateDTO {

    @Max(value = 999, message = "{constraint.maximum_length}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Integer id;
    @Size(max = 10, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String field;
    @Max(value = 999, message = "{constraint.maximum_length}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Integer initialPosition;
    @Max(value = 999, message = "{constraint.maximum_length}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Integer finalPosition;
    @Size(max = 20, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String creationUser;

    public Mono<AlertTemplate> toModel() {
        return Mono.just(AlertTemplate.builder()
                .id(this.id)
                .field(this.field)
                .initialPosition(this.initialPosition)
                .finalPosition(this.finalPosition)
                .creationUser(this.creationUser)
                .build());
    }
}
