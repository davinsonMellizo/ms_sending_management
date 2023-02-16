package co.com.bancolombia.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RecipientDTO {
    @Builder.Default
    private @Valid IdentificationDTO identification = new IdentificationDTO();
    @Builder.Default
    private ContactsDTO contacts = new ContactsDTO();
}
