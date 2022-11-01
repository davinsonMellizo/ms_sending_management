package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.template.dto.Template;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GetTemplateDTO {
    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 50, message = "Longitud maxima de 50 caracteres")
    private String idTemplate;

    public Mono<Template> toModel() {
        return Mono.just(Template.builder()
                .idTemplate(this.idTemplate)
                .build());
    }
}
