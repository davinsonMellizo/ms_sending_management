package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.providerservice.ProviderService;
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
public class ProviderServiceDTO {

    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    private Integer id;
    @Size(min = 1, max = 3, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idProvider;
    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer idService;

    public Mono<ProviderService> toModel() {
        return Mono.just(ProviderService.builder()
                .id(this.id)
                .idProvider(this.idProvider)
                .idService(this.idService)
                .build());
    }
}
