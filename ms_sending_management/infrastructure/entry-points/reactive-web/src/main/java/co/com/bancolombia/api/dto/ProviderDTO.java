package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.provider.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDTO {

    @Size(max = 3, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String id;
    @Size(max = 20, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String name;
    @Size(max = 1, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String typeService;
    @Size(max = 20, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String creationUser;

    public Mono<Provider> toModel() {
        return Mono.just(Provider.builder()
                .id(this.id)
                .name(this.name)
                .typeService(this.typeService)
                .creationUser(this.creationUser)
                .build());
    }
}
