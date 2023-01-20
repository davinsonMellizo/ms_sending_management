package co.com.bancolombia.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClientDTO {
    @Builder.Default
    private IdentificationDTO identification = new IdentificationDTO();
    @Builder.Default
    private ContactsDTO contacts = new ContactsDTO();
}
