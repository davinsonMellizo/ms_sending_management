package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.category.Category;
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
public class CategoryDTO extends DTO<Category> {

    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer id;
    @Size(min = 1, max = 20, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String name;
    @Size(min = 1, max = 20, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String creationUser;

    public Mono<Category> toModel() {
        return Mono.just(Category.builder()
                .id(this.id)
                .name(this.name)
                .creationUser(this.creationUser)
                .build());
    }
}
