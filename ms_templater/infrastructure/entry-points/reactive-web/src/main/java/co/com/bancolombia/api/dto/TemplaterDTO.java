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
    @NotNull(message = "campo obligatorio")
    @Size(min = 1, max = 10, message = "longitud maxima de 10 caracteres")
    private String id;

    @NotNull(message = "campo obligatorio")
    @Size(min = 1, max = 100, message = "longitud maxima de 100 caracteres")
    private String messageType;

    @NotNull(message = "campo obligatorio")
    @Size(min = 1, max = 100, message = "longitud maxima de 100 caracteres")
    private String messageSubject;

    @NotNull(message = "campo obligatorio")
    @Size(min = 1, max = 100, message = "longitud maxima de 100 caracteres")
    private String messageBody;

    @NotNull(message = "campo obligatorio")
    @Size(min = 1, max = 100, message = "longitud maxima de 100 caracteres")
    private String messageText;

    @NotNull(message = "campo obligatorio")
    @Size(min = 1, max = 100, message = "longitud maxima de 100 caracteres")
    private String creationUser;

    @NotNull(message = "campo obligatorio")
    @Size(min = 1, max = 100, message = "longitud maxima de 100 caracteres")
    private String consumerId;

    public Mono<TemplateRequest> toModel() {
        return Mono.just(TemplateRequest.builder()
                .id(this.id)
                .messageType(this.messageType)
                .messageSubject(this.messageSubject)
                .messageBody(this.messageBody)
                .messageText(this.messageText)
                .creationUser(this.creationUser)
                .consumerId(this.consumerId)
                .build());
    }
}
