package co.com.bancolombia.api.dto;

import lombok.*;
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

    public Mono<co.com.bancolombia.model.contact.Contact> toModel() {
        return Mono.just(co.com.bancolombia.model.contact.Contact.builder()
                .documentType(Integer.parseInt(this.documentType))
                .documentNumber(Long.parseLong(this.documentNumber))
                .contactMedium(this.contactMedium)
                .enrollmentContact(this.enrollmentContact)
                .value(this.value)
                .state(this.state)
                .build());
    }
}
