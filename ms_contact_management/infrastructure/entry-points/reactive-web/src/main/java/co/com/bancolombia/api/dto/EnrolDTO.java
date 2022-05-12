package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class EnrolDTO {

    @JsonProperty("customer")
    private @Valid ClientDTO client;

    private List<@Valid ContactDTO> contactData;
}
