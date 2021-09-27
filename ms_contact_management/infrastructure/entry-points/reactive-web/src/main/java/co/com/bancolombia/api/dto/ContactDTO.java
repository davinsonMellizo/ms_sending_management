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
    @Size(min = 1, max = 3, message = "{constraint.size}")
    private String consumer;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
    private String contactMedium;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 2, message = "{constraint.size}")
    private String documentType;
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
                .segment(this.consumer)
                .value(this.value)
                .state(this.state)
                .build());
    }
}
