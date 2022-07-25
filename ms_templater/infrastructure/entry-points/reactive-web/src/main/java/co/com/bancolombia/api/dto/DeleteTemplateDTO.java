package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.template.dto.Template;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor

public class DeleteTemplateDTO {
    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 50, message = "Longitud maxima de 50 caracteres")
    private String idTemplate;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 20, message = "Longitud maxima de 20 caracteres")
    private String user;


    public Mono<Template> toModel() {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        return Mono.just(Template.builder()
                .idTemplate(this.idTemplate)
                .modificationUser(this.user)
                .modificationDate(dateTime)
                .build());
    }
}
