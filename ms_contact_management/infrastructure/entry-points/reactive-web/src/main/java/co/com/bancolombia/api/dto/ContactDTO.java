package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.contact.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContactDTO {

    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @Size(max = 10, message = "{constraint.maximum_length}")
    private String enrollmentContact;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @Size(max = 10, message = "{constraint.maximum_length}")
    private String contactMedium;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    @Max(value = 99, message = "{constraint.maximum_value}")
    private Integer documentType;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Long documentNumber;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @Size(max = 60, message = "{constraint.maximum_length}")
    private String value;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @Size(max = 10, message = "{constraint.maximum_length}")
    private String state;

    public Mono<Contact> toModel() {
        return Mono.just(co.com.bancolombia.model.contact.Contact.builder()
                .documentType(this.documentType)
                .documentNumber(this.documentNumber)
                .contactMedium(this.contactMedium)
                .enrollmentContact(this.enrollmentContact)
                .value(this.value)
                .state(this.state)
                .build());
    }
}
