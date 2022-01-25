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

public class TemplaterDTO {
    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 50, message = "Longitud maxima de 50 caracteres")
    private String IdTemplate;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 20, message = "Longitud maxima de 20 caracteres")
    private String MessageType;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 10, message = "Longitud maxima de 10 caracteres")
    private String Version;

    @NotNull(message = "campo obligatorio")
    @Size(min = 1, max = 50, message = "longitud maxima de 50 caracteres")
    private String IdConsumer;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 200, message = "Longitud maxima de 200 caracteres")
    private String Description;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 500, message = "Longitud maxima de 500 caracteres")
    private String MessageSubject;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 50000, message = "Longitud maxima de 50000 caracteres")
    private String MessageBody;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 10000, message = "Longitud maxima de 10000 caracteres")
    private String PlainText;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 20, message = "Longitud maxima de 20 caracteres")
    private String CreationUser;

    @NotNull(message = "Campo obligatorio")
    @Size(min = 1, max = 20, message = "Longitud maxima de 20 caracteres")
    private String ModificationUser;


    public Mono<TemplateRequest> toModel() {
        return Mono.just(TemplateRequest.builder()
                .IdTemplate(this.IdTemplate)
                .MessageType(this.MessageType)
                .Version(this.Version)
                .IdConsumer(this.IdConsumer)
                .Description(this.Description)
                .MessageSubject(this.MessageSubject)
                .MessageBody(this.MessageBody)
                .PlainText(this.PlainText)
                .CreationUser(this.CreationUser)
                .ModificationUser(this.CreationUser)
                .build());
    }
}
