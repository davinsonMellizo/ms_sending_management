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

    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
    private String enrollmentContact;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
    private String contactMedium;
    @NotNull(message = "{constraint.not_null}")
    @Min(value = 0, message = "{constraint.min}")
    @Max(value = 99, message = "{constraint.max}")
    private Integer documentType;
    @NotNull(message = "{constraint.not_null}")
    @Min(value = 0, message = "{constraint.min}")
    @Max(value = 999999999999999L, message = "{constraint.max}")
    private Long documentNumber;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 60, message = "{constraint.size}")
    private String value;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
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
