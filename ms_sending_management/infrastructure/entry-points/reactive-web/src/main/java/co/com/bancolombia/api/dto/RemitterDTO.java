package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.remitter.Remitter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemitterDTO {

    @Max(value = 999, message = "{constraint.maximum_length}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Integer id;
    @Size(max = 70, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String mail;
    @Size(max = 10, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String state;
    @Size(max = 20, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String creationUser;

    public Mono<Remitter> toModel() {
        return Mono.just(Remitter.builder()
                .id(this.id)
                .mail(this.mail)
                .state(this.state)
                .creationUser(this.creationUser)
                .build());
    }
}
