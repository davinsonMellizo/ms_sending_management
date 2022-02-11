package co.com.bancolombia.api.dto;

import lombok.*;

import javax.validation.Valid;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class EnrolDTO {
    private @Valid ClientDTO client;
    private List<@Valid ContactDTO> contacts;
}
