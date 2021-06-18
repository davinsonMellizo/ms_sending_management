package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.provider.Provider;
import co.com.bancolombia.model.service.Service;
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
public class ServiceDTO {

    @Max(value = 999, message = "{constraint.maximum_length}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Integer id;
    @Size(max = 20, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String name;
    @Size(max = 20, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String creationUser;

    public Mono<Service> toModel() {
        return Mono.just(Service.builder()
                .id(this.id)
                .name(this.name)
                .creationUser(this.creationUser)
                .build());
    }
}
