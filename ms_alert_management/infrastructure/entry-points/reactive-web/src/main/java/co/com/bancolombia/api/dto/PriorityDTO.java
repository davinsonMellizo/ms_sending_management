package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.priority.Priority;
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
public class PriorityDTO extends DTO<Priority> {

    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    private Integer id;
    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer code;
    @Size(min = 1, max = 20, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String description;
    @Size(min = 1, max = 3, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idProvider;
    @Size(min = 1, max = 20, message = "{constraint.size}")
    private String creationUser;

    public Mono<Priority> toModel() {
        return Mono.just(Priority.builder()
                .id(this.id)
                .code(this.code)
                .description(this.description)
                .idProvider(this.idProvider)
                .creationUser(this.creationUser)
                .build());
    }
}
