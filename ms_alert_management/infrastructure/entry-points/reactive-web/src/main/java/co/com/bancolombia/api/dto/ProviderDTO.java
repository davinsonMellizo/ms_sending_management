package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.provider.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDTO extends DTO<Provider> {

    @Size(min = 1, max = 3, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String id;
    @Size(min = 1, max = 20, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String name;
    @Size(min = 1, max = 1, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String typeService;
    @Size(min = 1, max = 20, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String creationUser;

    @Override
    public Mono<Provider> toModel() {
        return Mono.just(Provider.builder()
                .id(this.id)
                .name(this.name)
                .typeService(this.typeService)
                .creationUser(this.creationUser)
                .build());
    }

}
