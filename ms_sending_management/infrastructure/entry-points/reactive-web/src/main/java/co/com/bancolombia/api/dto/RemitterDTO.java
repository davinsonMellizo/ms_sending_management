package co.com.bancolombia.api.dto;

import co.com.bancolombia.config.model.remitter.Remitter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemitterDTO {

    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer id;
    @Size(min = 1, max = 70, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String mail;
    @Size(min = 1, max = 10, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String state;
    @Size(min = 1, max = 20, message = "{constraint.size}")
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
