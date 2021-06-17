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
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @Size(max = 10, message = "{constraint.maximum_length}")
    private String contactMedium;
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    @Size(max = 10, message = "{constraint.maximum_length}")
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

