package co.com.bancolombia.events.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
