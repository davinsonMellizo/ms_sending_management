package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.client.Enrol;
import lombok.*;
import reactor.core.publisher.Mono;

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

    private Mono<Enrol> toModel(){

        return Mono.just(Enrol.builder()

                .build());
    }
}
