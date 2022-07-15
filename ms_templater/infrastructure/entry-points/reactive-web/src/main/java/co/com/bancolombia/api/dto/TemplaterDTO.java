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
public class TemplaterDTO {
    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 50, message = "Longitud maxima de 50 caracteres")
    private String idTemplate;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 20, message = "Longitud maxima de 20 caracteres")
    private String messageType;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 10, message = "Longitud maxima de 10 caracteres")
    private String version;

    @NotNull(message = "campo obligatorio")
    @Size(min = 1, max = 50, message = "longitud maxima de 50 caracteres")
    private String idConsumer;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 200, message = "Longitud maxima de 200 caracteres")
    private String description;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 500, message = "Longitud maxima de 500 caracteres")
    private String messageSubject;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 50000, message = "Longitud maxima de 50000 caracteres")
    private String messageBody;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 10000, message = "Longitud maxima de 10000 caracteres")
    private String plainText;

    @Size(min = 1, max = 20, message = "Longitud maxima de 20 caracteres")
    private String creationUser;

    @Size(min = 1, max = 20, message = "Longitud maxima de 20 caracteres")
    private String modificationUser;


    public Mono<Template> toModel() {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        return Mono.just(Template.builder()
                .idTemplate(this.idTemplate)
                .messageType(this.messageType)
                .version(this.version)
                .idConsumer(this.idConsumer)
                .description(this.description)
                .messageSubject(this.messageSubject)
                .messageBody(this.messageBody)
                .plainText(this.plainText)
                .creationUser(this.creationUser)
                .creationDate(dateTime)
                .modificationUser(this.modificationUser)
                .modificationDate(dateTime)
                .build());
    }
}
