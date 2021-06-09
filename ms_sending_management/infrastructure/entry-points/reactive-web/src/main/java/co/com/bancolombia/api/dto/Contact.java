package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.alert.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String enrollmentContact;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String contactMedium;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private String documentType;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private String documentNumber;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String value;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String state;

    public Mono<Alert> toModel() {
        return Mono.just(Alert.builder()
                .documentType(Integer.parseInt(this.documentType))
                .documentNumber(Long.parseLong(this.documentNumber))
                .contactMedium(this.contactMedium)
                .enrollmentContact(this.enrollmentContact)
                .value(this.value)
                .state(this.state)
                .build());
    }
}
