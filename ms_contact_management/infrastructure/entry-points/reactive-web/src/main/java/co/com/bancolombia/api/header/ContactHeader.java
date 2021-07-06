package co.com.bancolombia.api.header;

import co.com.bancolombia.model.contact.Contact;
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
public class ContactHeader {

    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number}")
    @Size(min = 1, max = 2, message = "{constraint.size}")
    private String documentType;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number}")
    @Size(min = 1, max = 15, message = "{constraint.size}")
    private String documentNumber;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
    private String contactMedium;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
    private String enrollmentContact;

    public Mono<Contact> toModel() {
        return Mono.just(Contact.builder()
                .documentType(Integer.parseInt(this.documentType))
                .documentNumber(Long.parseLong(this.documentNumber))
                .contactMedium(this.contactMedium)
                .enrollmentContact(this.enrollmentContact)
                .build());
    }
}

