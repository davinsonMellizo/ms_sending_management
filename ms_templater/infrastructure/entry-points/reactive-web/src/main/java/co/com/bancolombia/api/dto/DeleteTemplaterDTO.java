package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.template.dto.TemplateRequest;
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

public class DeleteTemplaterDTO {
    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 50, message = "Longitud maxima de 50 caracteres")
    private String idTemplate;


    public Mono<TemplateRequest> toModel() {
        return Mono.just(TemplateRequest.builder()
                .idTemplate(this.idTemplate)
                .build());
    }
}
